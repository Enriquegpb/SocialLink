package com.wilren.sociallink;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
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
import com.wilren.sociallink.Adaptador.AdaptadorMensaje;
import com.wilren.sociallink.Persona.Persona;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private RecyclerView listaMensajes;
    private AdaptadorMensaje adapter;
    private ArrayList<Persona> listaUsuarios = new ArrayList<>();
    private DatabaseReference db;
    private ArrayList<Persona> contactos;
    private FirebaseUser user;
    private final FirebaseDatabase INSTANCIA = FirebaseDatabase.getInstance("https://sociallink-2bf20-default-rtdb.europe-west1.firebasedatabase.app/");
    private CircleImageView searchView;
    private ArrayList<String> usuariosContactos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        INSTANCIA.setPersistenceEnabled(true);

        recuperarUsuarios();
        user = FirebaseAuth.getInstance().getCurrentUser();
        listaMensajes = findViewById(R.id.listaMensajes);
        searchView = findViewById(R.id.busquedaUsuarios);

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

        tv=findViewById(R.id.textView2);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t1=new Intent();

                Intent intent = new Intent(MainActivity.this, UserProfile.class);
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
                                usuariosContactos.add(persona.getId());
                                contactos.add(persona);
                                adapter.notifyItemRangeInserted(0, contactos.size());
                            }
                        }
                    }
                }
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

                    listaUsuarios.add(persona);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public Persona propiedadesMensaje(Persona persona) {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        Query query = rootRef.collection("chat")
                .document(user.getUid()).collection(persona.getId())
                .orderBy("time", Query.Direction.ASCENDING)
                .limit(1);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    persona.setUltimoMensaje(task.getResult().getDocuments().get(0).get("text").toString());
                }
            }
        });

        return persona;
    }

}