package com.wilren.sociallink;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wilren.sociallink.Adaptador.AdaptadorMensaje;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listaMensajes;
    private AdaptadorMensaje adapter;
    ArrayList <String> contactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        contactos = getIntent().getStringArrayListExtra("con");

        //Persona persona = getIntent().getParcelableExtra("persona");

        //mensajes = new ArrayList<>();
        /*
        mensajes.add(new Mensaje("Pepe", "Tue.", "Hola que tal"));
        mensajes.add(new Mensaje("Pepe", "Tue.", "Hola que tal"));
        mensajes.add(new Mensaje("Pepe", "Tue.", "Hola que tal"));
           */
        listaMensajes = findViewById(R.id.lista);

        adapter = new AdaptadorMensaje(contactos);

        listaMensajes.setLayoutManager(new LinearLayoutManager(this));
        listaMensajes.setAdapter(adapter);

    }
}