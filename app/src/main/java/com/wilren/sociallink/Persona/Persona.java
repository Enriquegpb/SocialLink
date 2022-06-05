package com.wilren.sociallink.Persona;

import android.os.Parcel;
import android.os.Parcelable;

import de.hdodenhof.circleimageview.CircleImageView;


public class Persona implements Parcelable {

    public String id, nombre, email, fotoPerfil, ultimoMensaje, fechaUltimoMensaje, descripcion, numeroTelefono;
    private CircleImageView perfil;

    public Persona(String id, String nombre, String email, String fotoPerfil) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.fotoPerfil = fotoPerfil;
    }

    public Persona(String id, String nombre, String email, String fotoPerfil, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.fotoPerfil = fotoPerfil;
        this.descripcion = descripcion;
    }
    public Persona(String id, String nombre, String email, String fotoPerfil, String descripcion, String numeroTelefono) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.fotoPerfil = fotoPerfil;
        this.descripcion = descripcion;
        this.numeroTelefono = numeroTelefono;
    }

    public Persona(){}

    protected Persona(Parcel in) {
        id = in.readString();
        nombre = in.readString();
        email = in.readString();
        fotoPerfil = in.readString();
        ultimoMensaje = in.readString();
        fechaUltimoMensaje = in.readString();
        descripcion = in.readString();
        numeroTelefono = in.readString();
    }

    public static final Creator<Persona> CREATOR = new Creator<Persona>() {
        @Override
        public Persona createFromParcel(Parcel in) {
            return new Persona(in);
        }

        @Override
        public Persona[] newArray(int size) {
            return new Persona[size];
        }
    };

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaUltimoMensaje() {
        return fechaUltimoMensaje;
    }

    public void setFechaUltimoMensaje(String fechaUltimoMensaje) {
        this.fechaUltimoMensaje = fechaUltimoMensaje;
    }

    public CircleImageView getPerfil() {
        return perfil;
    }

    public void setPerfil(CircleImageView perfil) {
        this.perfil = perfil;
    }

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    public void setUltimoMensaje(String ultimoMensaje) {
        this.ultimoMensaje = ultimoMensaje;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nombre);
        dest.writeString(email);
        dest.writeString(fotoPerfil);
        dest.writeString(ultimoMensaje);
        dest.writeString(fechaUltimoMensaje);
        dest.writeString(descripcion);
        dest.writeString(numeroTelefono);
    }
}
