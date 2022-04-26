package com.wilren.sociallink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.wilren.sociallink.Adaptador.AdaptadorMensaje;
import com.wilren.sociallink.Mensaje.Mensaje;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listaMensajes;
    private AdaptadorMensaje adapter;
    private ArrayList <Mensaje> mensajes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mensajes = new ArrayList<>();
        mensajes.add(new Mensaje("Pepe","Tue.","Hola que tal"));
        mensajes.add(new Mensaje("Pepe","Tue.","Hola que tal"));
        mensajes.add(new Mensaje("Pepe","Tue.","Hola que tal"));

        listaMensajes = findViewById(R.id.lista);

        adapter = new AdaptadorMensaje(mensajes);

        listaMensajes.setLayoutManager(new LinearLayoutManager(this));
        listaMensajes.setAdapter(adapter);

    }
}