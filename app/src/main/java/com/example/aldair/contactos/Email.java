package com.example.aldair.contactos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class Email extends AppCompatActivity {

    EditText txtEmail, txtSubject, txtMensaje;
    Button btnEnviar, btnInicio;
    ArrayList<Contactos> listaContactos;
    int posicion=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtSubject = (EditText) findViewById(R.id.txtSubject);
        txtMensaje = (EditText) findViewById(R.id.txtMensaje);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnInicio = (Button) findViewById(R.id.btnInicio);

        listaContactos =  new ArrayList<Contactos>();
        if(getIntent().getExtras() != null) {
            listaContactos = (ArrayList<Contactos>) getIntent().getExtras().getSerializable("lista");
            posicion = getIntent().getExtras().getInt("posicion");
            txtEmail.setText(listaContactos.get(posicion).getEmail().toString());
        }

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //es necesario un intent que levante la actividad deseada
                Intent itSend = new Intent(Intent.ACTION_SEND);
                //vamos a enviar texto plano
                itSend.setType("plain/text");
                //colocamos los datos para el envío
                itSend.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ txtEmail.getText().toString()});
                itSend.putExtra(android.content.Intent.EXTRA_SUBJECT, txtSubject.getText().toString());
                itSend.putExtra(android.content.Intent.EXTRA_TEXT, txtMensaje.getText().toString());
                //iniciamos la actividad
                startActivity(itSend);

                txtMensaje.setText("");
                txtSubject.setText("");

            }
        });

        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Email.this,Principal.class);
                if(!listaContactos.isEmpty()) {
                    i.putExtra("lista", listaContactos);
                    i.putExtra("posicion", posicion);
                }
                startActivity(i);
            }
        });
    }
}
