package com.example.aldair.contactos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Perfil extends AppCompatActivity {

    ImageView ivLlamar, ivMensaje, ivEmail, ivPerfilImagen;
    Button btnRegresar;
    TextView lbNumero, lbNombre, lbEmail;
    ArrayList<Contactos> listaContactos;
    int posicion=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        ivLlamar = (ImageView) findViewById(R.id.ivLlamar);
        btnRegresar = (Button) findViewById(R.id.btnRegresar);
        lbNumero = (TextView) findViewById(R.id.lbNumero);
        lbNombre = (TextView) findViewById(R.id.lbNombre);
        ivMensaje = (ImageView) findViewById(R.id.ivMensaje);
        lbEmail = (TextView) findViewById(R.id.lbEmail);
        ivEmail = (ImageView) findViewById(R.id.ivEmail);
        ivPerfilImagen = (ImageView) findViewById(R.id.ivPerfilImagen);

        //lbNombre.setText(getIntent().getExtras().getString("nombre"));
        //lbNumero.setText(getIntent().getExtras().getString("telefono"));
        listaContactos =  new ArrayList<Contactos>();
        String rutaImagen="";
        if(getIntent().getExtras() != null) {
            listaContactos = (ArrayList<Contactos>) getIntent().getExtras().getSerializable("lista");
            posicion = getIntent().getExtras().getInt("posicion");
            lbNombre.setText(listaContactos.get(posicion).getNombre().toString());
            lbNumero.setText(listaContactos.get(posicion).getTelefono().toString());
            lbEmail.setText(listaContactos.get(posicion).getEmail().toString());
            rutaImagen = listaContactos.get(posicion).getRutaImagen().toString();
        }

        File imgFile = new  File(rutaImagen);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ivPerfilImagen.setImageBitmap(myBitmap);
        }

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        ivLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLlamada(v);
            }
        });

        ivMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Perfil.this,Mensaje.class);
                if(!listaContactos.isEmpty()) {
                    i.putExtra("lista", listaContactos);
                    i.putExtra("posicion", posicion);
                }
                startActivity(i);
            }
        });

        ivEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Perfil.this,Email.class);
                if(!listaContactos.isEmpty()) {
                    i.putExtra("lista", listaContactos);
                    i.putExtra("posicion", posicion);
                }
                startActivity(i);
            }
        });

    }

    public void onClickLlamada(View v) {
        Intent i = new Intent(android.content.Intent.ACTION_CALL,
                Uri.parse("tel:"+lbNumero.getText().toString()));
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
