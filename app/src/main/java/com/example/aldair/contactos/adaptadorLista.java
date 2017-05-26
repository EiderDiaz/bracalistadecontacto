package com.example.aldair.contactos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class adaptadorLista extends BaseAdapter {


    Context contexto;
    ArrayList<Contactos> listaContactos;

    public adaptadorLista(Context contexto, ArrayList<Contactos> listaContactos) {
        this.contexto = contexto;
        this.listaContactos = listaContactos;
    }

    @Override
    public int getCount() {
        return listaContactos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaContactos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View renglon=convertView;
        if(convertView==null){
            // se infla
            LayoutInflater inflador= (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            renglon=inflador.inflate(R.layout.renglon,parent,false);
        }
        TextView tvNombre=(TextView) renglon.findViewById(R.id.tvNombre);
        ImageView ivPerfil = (ImageView) renglon.findViewById(R.id.imgPerfil);
        tvNombre.setText(listaContactos.get(position).getNombre());
        File imgFile = new  File(listaContactos.get(position).getRutaImagen());
        if(imgFile.exists() && !listaContactos.isEmpty()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ivPerfil.setImageBitmap(myBitmap);
        }

        return renglon;
    }
}
