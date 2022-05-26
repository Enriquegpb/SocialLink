package com.wilren.sociallink;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private ArrayList <Persona> listaContactos;
    private ArrayList <String> contactos;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactos = getIntent().getStringArrayListExtra("con");
        listaMensajes = findViewById(R.id.listaMensajes);
        recuperarUsuarios();

        adapter = new AdaptadorMensaje(listaContactos);
        listaMensajes.setLayoutManager(new LinearLayoutManager(this));

    }

    public void recuperarUsuarios(){

        db = FirebaseDatabase.getInstance("https://sociallink-2bf20-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");
        listaContactos = new ArrayList<>();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                        String id = dataSnapshot.child("id").getValue().toString();

                        for (int i = 0; i < contactos.size(); i++){
                            if(id.equals(contactos.get(i))){
                                Persona persona = new Persona();
                                persona.setNombre(dataSnapshot.child("nombre").getValue().toString());
                                persona.setId(dataSnapshot.child("id").getValue().toString());
                                //persona.setFotoPerfil(dataSnapshot.child("perfil").getValue().toString());
                                persona.setEmail(dataSnapshot.child("email").getValue().toString());
                                listaContactos.add(persona);
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


}