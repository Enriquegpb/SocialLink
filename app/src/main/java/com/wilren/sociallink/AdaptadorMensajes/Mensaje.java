package com.wilren.sociallink.AdaptadorMensajes;

public class Mensaje {

    private String nombre;
    private String fecha;
    private String ultMensaje;
    private String identificador;

    //private String fotoPerfil;

    public Mensaje(String nombre, String fecha, String ultMensaje){
        this.nombre = nombre;
        this.fecha = fecha;
        this.ultMensaje = ultMensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getUltMensaje() {
        return ultMensaje;
    }

    public void setUltMensaje(String ultMensaje) {
        this.ultMensaje = ultMensaje;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
}
