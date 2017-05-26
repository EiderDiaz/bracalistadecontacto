package com.example.aldair.contactos;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Digits;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AgregarContacto extends AppCompatActivity implements Validator.ValidationListener {
    @NotEmpty(message = "debes de llenar este campo")
    private  EditText txtNombre;
    @Digits(message = "tiene que ser un numero telefonico de 10 digitos",integer = 10)
    private EditText txtTelefono;
    @com.mobsandgeeks.saripaar.annotation.Email(message = "esto no es un correo valido")
    private EditText txtEmail;
    ImageView ivCancelar;
    ImageView ivAceptar;
    ImageView ivImagen;
    Validator validator;
    private static final int SELECT_FILE = 1;
    String nombreImagen = "";
    String pathImagen = "";
    AdminSQLiteOpenHelper admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto);

        ivCancelar = (ImageView) findViewById(R.id.ivCancelar);
        ivAceptar = (ImageView) findViewById(R.id.ivAceptar);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        ivImagen = (ImageView) findViewById(R.id.ivImagen);

        ivCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        validator= new Validator(this);
        validator.setValidationListener(this);
        ivAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        validator.validate();

            }
        });

        ivImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria(v);
            }
        });
    }

    public void insertarContacto(String nombre,String telefono,String email,String rutaImg){
        admin = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = admin.getWritableDatabase();
        admin.insertarContacto(db,nombre, telefono, email, rutaImg);
    }

    public void abrirGaleria(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Seleccione una imagen"),SELECT_FILE);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Uri selectedImageUri = null;
        Uri selectedImage;

        String filePath = null;
        switch (requestCode) {
            case SELECT_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImage = imageReturnedIntent.getData();
                    nombreImagen = selectedImage.getLastPathSegment();
                    if (requestCode == SELECT_FILE) {

                        if (selectedImage != null) {
                            InputStream imageStream = null;
                            try {
                                imageStream = getContentResolver().openInputStream(
                                        selectedImage);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            // Transformamos la URI de la imagen a inputStream y este a un Bitmap
                            Bitmap bmp = BitmapFactory.decodeStream(imageStream);

                            // Ponemos nuestro bitmap en un ImageView que tengamos en la vista
                            //ImageView mImg = (ImageView) findViewById(R.id.ivImagen);
                            ivImagen.setImageBitmap(bmp);

                        }
                    }
                }
                break;
        }
    }

    File dirImages;
    private String guardarImagen (Context context, String nombre, Bitmap imagen){
        ContextWrapper cw = new ContextWrapper(context);
        dirImages = cw.getDir("Imagenes", Context.MODE_PRIVATE);
        File myPath = new File(dirImages, nombre +".png");
        pathImagen = myPath.getAbsolutePath();
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(myPath);
            imagen.compress(Bitmap.CompressFormat.JPEG, 10, fos);
            fos.flush();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return myPath.getAbsolutePath();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       //dirImages.delete();
    }

    @Override
    public void onValidationSucceeded() {
        Bitmap imagen = ((BitmapDrawable)ivImagen.getDrawable()).getBitmap();
        guardarImagen(getApplicationContext(), nombreImagen, imagen);

        String nombre = txtNombre.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String email = txtEmail.getText().toString();
        Intent i = getIntent();
        i.putExtra("nombre", nombre);
        i.putExtra("telefono", telefono);
        i.putExtra("email", email);
        i.putExtra("ruta",pathImagen);
        setResult(RESULT_OK, i);

        insertarContacto(nombre,telefono,email,pathImagen);

        finish();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
