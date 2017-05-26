package com.example.aldair.contactos;


public class LogicaBD {

    private final static String NOMBRE_TABLA="tblcontactos";
    private final static String ID="id";
    private final static String NOMBRE="nombre";
    private final static String TELEFONO="telefono";
    private final static String EMAIL="email";
    private final static String RUTA_IMG="rutaImg";

    /*public final static String CREAR_TABLA_CONTACTOS="create table "+NOMBRE_TABLA+"("
            +ID+" integer not null primary key autoincrement, "
            +NOMBRE+" text, "
            +TELEFONO+" text, "
            +EMAIL+" text, "
            +RUTA_IMG+" text);";*/
    public static  final String CREARTABLACONTACTOS = "create table "+NOMBRE_TABLA+"("
            +ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            +NOMBRE+" TEXT NOT NULL, "
            +TELEFONO+" TEXT, "
            +EMAIL+" TEXT, "
            +RUTA_IMG+")";

    public static String insertarContacto(String nombre, String telefono, String email, String rutaImg){
        String consulta = "insert into "+NOMBRE_TABLA+"("
                +NOMBRE+","
                +TELEFONO+","
                +EMAIL+","
                +RUTA_IMG+")"
                +" values('"+nombre+"','"+telefono+"','"+email+"','"+rutaImg+"');";
        return consulta;
    }

    public static String BorrarContacto(String telefono){
        String borrar="DELETE  FROM "+NOMBRE_TABLA+" WHERE "+TELEFONO+" = "+telefono;
        return  borrar;
    }

    public static String UpdateContacto(String telefono,String nombre,String email,String imagenruta){
        String update = "UPDATE"+NOMBRE_TABLA
                +" SET"+ NOMBRE+"="+ nombre+","
                +" SET"+ EMAIL+"="+ email+","
                +" SET"+ RUTA_IMG+"="+ imagenruta+","
                +  "WHERE"+TELEFONO+ "="+telefono;

            return  update;
    }
}
