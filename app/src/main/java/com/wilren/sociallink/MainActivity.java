package com.wilren.sociallink;


import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wilren.sociallink.Adaptador.AdaptadorMensaje;
import com.wilren.sociallink.Persona.Persona;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listaMensajes;
    private AdaptadorMensaje adapter;
    private ArrayList <Persona> listaContactos = new ArrayList<>();
    private DatabaseReference db;
    Persona usuario;
    ArrayList <String> contactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario = getIntent().getParcelableExtra("persona");
        recuperarUsuarios2();

        listaMensajes = findViewById(R.id.listaMensajes);

        adapter = new AdaptadorMensaje(listaContactos);
        listaMensajes.setLayoutManager(new LinearLayoutManager(this));

    }


    public void recuperarUsuarios2(){
        contactos = new ArrayList<>();

        DatabaseReference data = FirebaseDatabase.getInstance("https://sociallink-2bf20-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Contactos").child(usuario.getId());

        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    contactos.add(dataSnapshot.getKey());
                }
                recuperarUsuarios();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void recuperarUsuarios() {

        db = FirebaseDatabase.getInstance("https://sociallink-2bf20-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String id = dataSnapshot.child("id").getValue().toString();
                    for (int i = 0; i < contactos.size(); i++) {
                        if (id.equals(usuario.getId())) {
                            Persona persona = new Persona();
                            persona.setNombre(dataSnapshot.child("nombre").getValue().toString());
                            persona.setId(id);
                            //persona.setFotoPerfil(dataSnapshot.child("perfil").getValue().toString());
                            persona.setEmail(dataSnapshot.child("email").getValue().toString());
                            listaContactos.add(persona);
                        }
                    }
                    listaMensajes.setAdapter(adapter);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}