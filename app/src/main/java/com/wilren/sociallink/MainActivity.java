package com.wilren.sociallink;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.wilren.sociallink.Adaptador.AdaptadorMensaje;
import com.wilren.sociallink.Persona.Persona;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listaMensajes;
    private AdaptadorMensaje adapter;
    private ArrayList<Persona> listaUsuarios = new ArrayList<>();
    private DatabaseReference db;
    private ArrayList<Persona> contactos;
    private FirebaseUser user;
    private final FirebaseDatabase INSTANCIA = FirebaseDatabase.getInstance("https://sociallink-2bf20-default-rtdb.europe-west1.firebasedatabase.app/");
    private CircleImageView searchView, usuarioActual;
    private ArrayList<String> usuariosContactos;
    private Persona personaActual = new Persona();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        INSTANCIA. setPersistenceEnabled(true);

        recuperarUsuarios();
        user = FirebaseAuth.getInstance().getCurrentUser();
        listaMensajes = findViewById(R.id.listaMensajes);
        searchView = findViewById(R.id.busquedaUsuarios);
        usuarioActual = findViewById(R.id.usuarioActual);

        contactos = new ArrayList<>();
        usuariosContactos = new ArrayList<>();

        adapter = new AdaptadorMensaje(contactos, MainActivity.this, user.getUid());
        cargaUsuarios();
        listaMensajes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BusquedaUsuariosActivity.class);
                intent.putExtra("contactos", usuariosContactos);
                startActivity(intent);
            }
        });

        usuarioActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserProfile.class);
                intent.putExtra("personaActual", personaActual);
                startActivity(intent);
            }
        });
    }

    public void cargaUsuarios() {
        DatabaseReference data = INSTANCIA.getReference("Contactos").child(user.getUid());
        data.keepSynced(true);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contactos.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (int i = 0; i < listaUsuarios.size(); i++) {
                        if (dataSnapshot.getKey().equals(listaUsuarios.get(i).getId())) {
                            Persona persona = listaUsuarios.get(i);
                            if (!contactos.contains(persona)) {
                                if(dataSnapshot.hasChild("fecha")){
                                    persona.setFechaUltimoMensaje(dataSnapshot.child("fecha").getValue().toString());
                                }else {
                                    persona.setFechaUltimoMensaje("");
                                }
                                if(dataSnapshot.hasChild("ultimoMensaje")){
                                    persona.setUltimoMensaje(dataSnapshot.child("ultimoMensaje").getValue().toString());
                                }else{
                                    persona.setUltimoMensaje("");
                                }

                                usuariosContactos.add(persona.getId());
                                contactos.add(persona);
                                adapter.notifyItemRangeInserted(0, contactos.size());
                            }
                        }
                    }
                }
                adapter.notifyItemRangeInserted(0, contactos.size());
                adapter.notifyDataSetChanged();
                listaMensajes.setAdapter(adapter);
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
                listaUsuarios.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String id = dataSnapshot.child("id").getValue().toString();
                    String nombre = dataSnapshot.child("nombre").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    String fotoPerfil = dataSnapshot.child("fotoPerfil").getValue().toString();

                    Persona persona = new Persona();
                    persona.setEmail(email);
                    persona.setNombre(nombre);
                    persona.setId(id);
                    persona.setFotoPerfil(fotoPerfil);

                    if(dataSnapshot.hasChild("numeroTelefono")){
                        String telefono = dataSnapshot.child("numeroTelefono").getValue().toString();
                        if(!telefono.isEmpty()) {
                            persona.setNumeroTelefono(Integer.parseInt(telefono));
                        }else{
                            persona.setNumeroTelefono(0);
                        }
                    }else{
                        persona.setNumeroTelefono(0);
                    }

                    if (user.getUid().equals(id)){
                        personaActual = persona;
                        if(!fotoPerfil.isEmpty()){
                            Picasso.get().load(persona.getFotoPerfil()).placeholder(R.drawable.user).into(usuarioActual);
                        }
                    }
                    listaUsuarios.add(persona);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}