package com.example.aldair.contactos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Mensaje extends AppCompatActivity {

    EditText txtMensaje;
    TextView lbNombre, lbTelefono;
    Button btnEnviar, btnCancelar;
    ArrayList<Contactos> listaContactos;
    int posicion = 0;
    String telefono = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje);

        txtMensaje = (EditText) findViewById(R.id.txtMensaje);
        lbNombre = (TextView) findViewById(R.id.txtNombre);
        lbTelefono = (TextView) findViewById(R.id.txtTelefono);
       btnEnviar = (Button) findViewById(R.id.btnEnviar);
       btnCancelar = (Button) findViewById(R.id.btnCancelar);


        listaContactos =  new ArrayList<Contactos>();
        if(getIntent().getExtras() != null) {
            listaContactos = (ArrayList<Contactos>) getIntent().getExtras().getSerializable("lista");
            posicion = getIntent().getExtras().getInt("posicion");
            lbNombre.setText(listaContactos.get(posicion).getNombre().toString());
            lbTelefono.setText(listaContactos.get(posicion).getTelefono().toString());
            telefono = listaContactos.get(posicion).getTelefono().toString();
        }

       /*String nombre = getIntent().getExtras().getString("nombre").toString();
        final String telefono = getIntent().getExtras().getString("telefono").toString();
        lbNombre.setText(nombre);
        lbTelefono.setText(telefono);*/

       btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtMensaje.getText().toString().equals("") && !telefono.toString().equals("")) {
                    enviarMensaje(telefono, txtMensaje.getText().toString());
                    txtMensaje.setText("");
                    Toast.makeText(Mensaje.this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Mensaje.this, "Mensaje vacio", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Mensaje.this,Principal.class);
                if(!listaContactos.isEmpty()) {
                    i.putExtra("lista", listaContactos);
                    i.putExtra("posicion", posicion);
                }
                startActivity(i);
            }
        });
    }


    private void enviarMensaje (String Numero, String Mensaje){
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(Numero,null,Mensaje,null,null);
            Toast.makeText(getApplicationContext(), "Mensaje Enviado.", Toast.LENGTH_LONG).show();
        }

        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Mensaje no enviado, datos incorrectos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
}
