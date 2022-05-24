package com.wilren.sociallink;

import android.os.Bundle;
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
    private ArrayList <String> contactos;
    private ArrayList <Persona> listaContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactos = getIntent().getStringArrayListExtra("con");
        listaContactos = new ArrayList<>();
        listaContactos = recuperarUsuarios();

        adapter = new AdaptadorMensaje(listaContactos);
        listaMensajes = findViewById(R.id.lista);

        //Toast.makeText(this, contactos.toString(), Toast.LENGTH_SHORT).show();

        listaMensajes.setLayoutManager(new LinearLayoutManager(this));
        listaMensajes.setAdapter(adapter);

    }

    public ArrayList recuperarUsuarios(){

        DatabaseReference db = FirebaseDatabase.getInstance("https://sociallink-2bf20-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");
        listaContactos = new ArrayList<>();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    Persona persona = new Persona();
                    persona.setNombre(dataSnapshot.child("nombre").getValue().toString());
                    listaContactos.add(persona);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return listaContactos;

    }


}