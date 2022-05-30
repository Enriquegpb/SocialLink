package com.wilren.sociallink.Persona;

import android.os.Parcel;
import android.os.Parcelable;


public class Persona implements Parcelable {

    public String id, nombre, email, fotoPerfil, ultimoMensaje;

    public Persona(String id, String nombre, String email, String fotoPerfil) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.fotoPerfil = fotoPerfil;
    }
    public Persona(String id, String nombre, String email, String fotoPerfil, String ultimoMensaje) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.fotoPerfil = fotoPerfil;
        this.ultimoMensaje = ultimoMensaje;
    }

    public Persona(){}


    protected Persona(Parcel in) {
        id = in.readString();
        nombre = in.readString();
        email = in.readString();
        fotoPerfil = in.readString();
        ultimoMensaje = in.readString();
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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(nombre);
        parcel.writeString(email);
        parcel.writeString(fotoPerfil);
        parcel.writeString(ultimoMensaje);
    }
}
