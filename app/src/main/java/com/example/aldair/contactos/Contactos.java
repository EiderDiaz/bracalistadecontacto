package com.example.aldair.contactos;


import android.os.Parcel;
import android.os.Parcelable;

public class Contactos implements Parcelable{

    private String nombre, email, telefono, rutaImagen;

    public Contactos(){
        this.nombre = "";
        this.telefono = "";
        this.email = "";
        this.rutaImagen = "";
    }

    public Contactos(String nombre, String telefono, String email, String rutaImagen){
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.rutaImagen = rutaImagen;
    }

    protected Contactos(Parcel in) {
        nombre = in.readString();
        email = in.readString();
        telefono = in.readString();
        rutaImagen = in.readString();
    }

    public static final Creator<Contactos> CREATOR = new Creator<Contactos>() {
        @Override
        public Contactos createFromParcel(Parcel in) {
            return new Contactos(in);
        }

        @Override
        public Contactos[] newArray(int size) {
            return new Contactos[size];
        }
    };

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(email);
        dest.writeString(telefono);
        dest.writeString(rutaImagen);
    }
}
