package com.wilren.sociallink;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.wilren.sociallink.Adaptador.AdaptadorMensaje;
import com.wilren.sociallink.Persona.Persona;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listaMensajes;
    private AdaptadorMensaje adapter;
    private ArrayList <Persona> listaContactos = new ArrayList<>();
    private DatabaseReference db;
    private ArrayList <String> contactos;
    private FirebaseUser user;
    private final FirebaseDatabase INSTANCIA = FirebaseDatabase.getInstance("https://sociallink-2bf20-default-rtdb.europe-west1.firebasedatabase.app/");
    private SearchView busquedaUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        Toast.makeText(getApplicationContext(), user.getEmail(), Toast.LENGTH_SHORT).show();
        listaMensajes = findViewById(R.id.listaMensajes);
//        busquedaUsuarios = findViewById(R.id.busquedaUsuarios);

        cargaUsuarios();

        adapter = new AdaptadorMensaje(listaContactos, user.getUid());
        listaMensajes.setLayoutManager(new LinearLayoutManager(this));

    }

    public void cargaUsuarios(){
        contactos = new ArrayList<>();

        DatabaseReference data = INSTANCIA.getReference("Contactos").child(user.getUid());

        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    contactos.add(dataSnapshot.getKey());
                }
                recuperarUsuarios();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void recuperarUsuarios() {

        db = INSTANCIA.getReference("Users");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String id = dataSnapshot.child("id").getValue().toString();
                    String nombre = dataSnapshot.child("nombre").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();

                    Persona persona = new Persona();
                    persona.setNombre(nombre);
                    persona.setId(id);
                    //persona.setFotoPerfil(dataSnapshot.child("perfil").getValue().toString());
                    persona.setEmail(email);

                    for (int i = 0; i < contactos.size(); i++) {
                        if (id.equals(contactos.get(i))) {
                            listaContactos.add(persona);
                        }
                    }
                    listaMensajes.setAdapter(adapter);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }


}