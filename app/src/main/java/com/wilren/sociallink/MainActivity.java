package com.wilren.sociallink;


import android.os.Bundle;
import android.widget.SearchView;

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
    private ArrayList<Persona> listaUsuarios = new ArrayList<>();
    private DatabaseReference db;
    private ArrayList<Persona> contactos;
    private FirebaseUser user;
    private final FirebaseDatabase INSTANCIA = FirebaseDatabase.getInstance("https://sociallink-2bf20-default-rtdb.europe-west1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recuperarUsuarios();
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();
        listaMensajes = findViewById(R.id.listaMensajes);
        contactos = new ArrayList<>();

        cargaUsuarios();

        adapter = new AdaptadorMensaje(contactos);
        listaMensajes.setAdapter(adapter);
        listaMensajes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


    }

    public void cargaUsuarios() {
        DatabaseReference data = INSTANCIA.getReference("Contactos").child(user.getUid());
        data.keepSynced(true);

        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if(dataSnapshot.hasChildren()){
                        for (int i = 0; i < listaUsuarios.size(); i++) {
                            if(dataSnapshot.getKey().equals(listaUsuarios.get(i).getId())){
                                Persona persona = listaUsuarios.get(i);
                                persona.setUltimoMensaje(dataSnapshot.child("ultimoMensaje").getValue().toString());
                                if(!contactos.contains(persona)){
                                    contactos.add(persona);
                                }else{
                                    for (int j = 0; j < contactos.size(); j++) {
                                        Persona p = contactos.get(j);
                                        persona.setUltimoMensaje(dataSnapshot.child("ultimoMensaje").getValue().toString());
                                        contactos.set(j, p);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }
                }
                adapter.notifyItemRangeInserted(0, contactos.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void recuperarUsuarios() {

        db = INSTANCIA.getReference("Users");
        db.keepSynced(true);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String id = dataSnapshot.child("id").getValue().toString();
                    String nombre = dataSnapshot.child("nombre").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();

                    Persona persona = new Persona();
                    persona.setEmail(email);
                    persona.setNombre(nombre);
                    persona.setId(id);
                    listaUsuarios.add(persona);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

}