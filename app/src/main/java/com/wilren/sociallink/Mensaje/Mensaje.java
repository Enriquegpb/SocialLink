package com.wilren.sociallink.Mensaje;

public class Mensaje {
    private String nombre;
    private String fecha;
    private String ultMensaje;

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
}
