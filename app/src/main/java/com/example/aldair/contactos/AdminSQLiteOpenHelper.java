package com.example.aldair.contactos;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {


    private static final String NOMBREBD = "contactos.bd";
    private static final int VERSIONBD=1;

    public AdminSQLiteOpenHelper(Context context) {
        super(context, NOMBREBD, null, VERSIONBD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LogicaBD.CREARTABLACONTACTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertarContacto(SQLiteDatabase db, String nombre, String telefono, String email, String rutaImg){
        db.execSQL(LogicaBD.insertarContacto(nombre, telefono, email, rutaImg));
        db.close();
    }
    public void BorrarContacto(SQLiteDatabase db, String telefono){
        db.execSQL(LogicaBD.BorrarContacto(telefono));
    }

    public void UpdateContacto(SQLiteDatabase db,String telefono,String nombre,String email,String imagenruta){
       db.execSQL(LogicaBD.UpdateContacto(telefono,nombre,email,imagenruta));
    }

    ArrayList<Contactos> listaContactos = new ArrayList<>();
    public ArrayList consultaTodo(SQLiteDatabase db){

        Cursor fila = db.rawQuery("select nombre,telefono,email,rutaImg from tblcontactos", null);
        if (fila.moveToFirst()) {
            listaContactos.clear();
            do {
                listaContactos.add(new Contactos(fila.getString(0),fila.getString(1),fila.getString(2),fila.getString(3)));
            }while (fila.moveToNext());
        }
        db.close();
        return listaContactos;
    }


}
