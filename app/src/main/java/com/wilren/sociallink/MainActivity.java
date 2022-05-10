package com.wilren.sociallink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wilren.sociallink.AdaptadorMensajes.AdaptadorMensaje;
import com.wilren.sociallink.AdaptadorMensajes.Mensaje;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listaMensajes;
    private AdaptadorMensaje adapter;
    private ArrayList <Mensaje> mensajes;

    DatabaseReference dbReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://sociallink-2bf20-default-rtdb.europe-west1.firebasedatabase.app");

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

        String identificador;

        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}