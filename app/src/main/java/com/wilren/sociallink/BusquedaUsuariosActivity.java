package com.wilren.sociallink;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import com.wilren.sociallink.AdaptadorBusquedaUsuario.AdapterBusquedaUsuario;
import com.wilren.sociallink.Persona.Persona;

import java.util.ArrayList;
import java.util.Locale;

public class BusquedaUsuariosActivity extends AppCompatActivity {

    private SearchView searchView;
    private AdapterBusquedaUsuario adapter;
    private RecyclerView listaBusquedaUsuario;
    private ArrayList <Persona> listaPersonas;
    private final FirebaseDatabase INSTANCIA = FirebaseDatabase.getInstance("https://sociallink-2bf20-default-rtdb.europe-west1.firebasedatabase.app/");
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ArrayList <String> listaContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busquedausuario);

        listaContactos = getIntent().getStringArrayListExtra("contactos");
        listaContactos.add(user.getUid());

        searchView = findViewById(R.id.searchView);
        listaBusquedaUsuario = findViewById(R.id.listaBusquedaUsuarios);

        rellenoPersonas();

        adapter = new AdapterBusquedaUsuario(listaPersonas, BusquedaUsuariosActivity.this);
        listaBusquedaUsuario.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                listarUsuarios(newText);
                return true;
            }
        });

    }


    public void rellenoPersonas() {
        listaPersonas = new ArrayList<>();

        DatabaseReference db = INSTANCIA.getReference("Users");
        db.keepSynced(true);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if(!listaContactos.contains(dataSnapshot.getKey())) {
                        String id = dataSnapshot.child("id").getValue().toString();
                        String nombre = dataSnapshot.child("nombre").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();
                        String fotoPerfil = dataSnapshot.child("fotoPerfil").getValue().toString();

                        Persona persona = new Persona();
                        persona.setEmail(email);
                        persona.setNombre(nombre);
                        persona.setId(id);
                        persona.setFotoPerfil(fotoPerfil);

                        listaPersonas.add(persona);
                        adapter.notifyItemRangeInserted(0, listaContactos.size());
                    }
                }
                listaBusquedaUsuario.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void listarUsuarios(String text){
        ArrayList <Persona> personas = new ArrayList<>();
        for (Persona i:listaPersonas) {
            if(i.getNombre().toLowerCase().contains(text.toLowerCase())){
                personas.add(i);
            }
        }
        if(personas.isEmpty()){
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }else{
            adapter.setFilteredList(personas);
        }
    }

}
