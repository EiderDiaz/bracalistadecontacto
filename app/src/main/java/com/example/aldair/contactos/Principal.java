package com.example.aldair.contactos;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class Principal extends AppCompatActivity {

    ListView lv;
    ImageView agregarContacto, imgPerfil;
    String pathImagen = "";
    AdminSQLiteOpenHelper admin;
    ArrayList<Contactos> listaContactos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        lv = (ListView) findViewById(R.id.lvNombres);
        listaContactos =  new ArrayList<>();

        agregarContacto = (ImageView) findViewById(R.id.AddUser);
        imgPerfil = (ImageView) findViewById(R.id.imgPerfil);
       admin = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = admin.getWritableDatabase();

        if(listaContactos.isEmpty()) {
            listaContactos = admin.consultaTodo(db);
            adaptadorLista adaptadorBD = new adaptadorLista(this, listaContactos);
            lv.setAdapter(adaptadorBD);
        }

        if(getIntent().getExtras() != null) {
            if(getIntent().getExtras().containsKey("lista")) {
                listaContactos = (ArrayList<Contactos>) getIntent().getExtras().getSerializable("lista");
                adaptadorLista adaptador1 = new adaptadorLista(this, listaContactos);
                lv.setAdapter(adaptador1);
            }
        }

        agregarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Principal.this, AgregarContacto.class);
                startActivityForResult(i,1);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nombre = listaContactos.get(position).getNombre();
                Toast.makeText(Principal.this, nombre, Toast.LENGTH_SHORT).show();
            }
        });

        registerForContextMenu(lv);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 1:
                    String nombre = data.getExtras().getString("nombre").toString();
                    String telefono = data.getExtras().getString("telefono").toString();
                    String email = data.getExtras().getString("email").toString();
                    pathImagen = data.getExtras().getString("ruta").toString();
                    listaContactos.add(new Contactos(nombre,telefono,email,pathImagen));

                    adaptadorLista adaptador = new adaptadorLista(this, listaContactos);
                    lv.setAdapter(adaptador);

                    break;
                case 3:
                    String nombreEditar = data.getExtras().getString("nombre").toString();
                    String telefonoEditar = data.getExtras().getString("telefono").toString();
                    String emailEditar = data.getExtras().getString("email").toString();
                    int posicionContacto = data.getExtras().getInt("posicionContacto");
                    String rutaImagen = data.getExtras().getString("rutaImagen").toString();
                    listaContactos.set(posicionContacto, new Contactos(nombreEditar,telefonoEditar,emailEditar,rutaImagen));
                    adaptadorLista adaptadorEditar = new adaptadorLista(this, listaContactos);
                    lv.setAdapter(adaptadorEditar);
                    break;
                default:
                    break;
            }

        }else if(resultCode == RESULT_CANCELED){
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //Menu: Trae el menu vacio para su posterior llenado
        //v: Objeto que lanzo el menu
        //menuInfo: Trae toda la informacion del objeto que lanzo el menu
        if(v.getId() == R.id.lvNombres){
            AdapterView.AdapterContextMenuInfo infoContacto = (AdapterView.AdapterContextMenuInfo) menuInfo;
            String[] elementosMenu =  getResources().getStringArray(R.array.elementosMenuContextual);
            menu.setHeaderTitle(listaContactos.get(infoContacto.position).getNombre());
            for (int i=0;i<elementosMenu.length;i++){
                menu.add(Menu.NONE,i,i,elementosMenu[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //item: Cual es el elemento del menu contextual que seleccionamos
        final AdapterView.AdapterContextMenuInfo infoContacto = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String[] elementosMenu =  getResources().getStringArray(R.array.elementosMenuContextual);
        int posicionElementoSeleccionado = item.getItemId();
        String opcionSeleccionada = elementosMenu[posicionElementoSeleccionado];
        String contactoSeleccionado = listaContactos.get(infoContacto.position).getNombre();
        switch (opcionSeleccionada){
            case "Editar":
                Intent iE = new Intent(Principal.this, Editar.class);
                iE.putExtra("nombre", listaContactos.get(infoContacto.position).getNombre());
                iE.putExtra("telefono", listaContactos.get(infoContacto.position).getTelefono());
                iE.putExtra("email", listaContactos.get(infoContacto.position).getEmail());
                iE.putExtra("posicionContacto", infoContacto.position);
                iE.putExtra("rutaImagen", listaContactos.get(infoContacto.position).getRutaImagen());
                startActivityForResult(iE,3);
                break;
            case "Mensaje de texto":
                Intent i = new Intent(Principal.this,Mensaje.class);
                if(!listaContactos.isEmpty()) {
                    i.putExtra("lista", listaContactos);
                    i.putExtra("posicion", infoContacto.position);
                }
                startActivity(i);
                break;
            case "Eliminar":
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Decea eliminar el contacto?")
                        .setTitle("Advertencia")
                        .setCancelable(false)
                        .setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                        .setPositiveButton("Continuar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                       listaContactos.remove(infoContacto.position);
                                        adaptadorLista adaptador = new adaptadorLista(Principal.this, listaContactos);
                                        lv.setAdapter(adaptador);
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case "Llamar":
                onClickLlamada(listaContactos.get(infoContacto.position).getTelefono());
                break;
            default:
                break;
        }

        return true;
    }

    public void onClickLlamada(String numero) {
        Intent i = new Intent(android.content.Intent.ACTION_CALL,
                Uri.parse("tel:"+numero));
        //Intent i = new Intent(android.content.Intent.ACTION_DIAL,
        //      Uri.parse("tel:+668..."));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(i);
    }
}
