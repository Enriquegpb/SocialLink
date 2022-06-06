package com.wilren.sociallink;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wilren.sociallink.Persona.Persona;

import java.text.MessageFormat;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserContactProfile extends AppCompatActivity {

    private Persona contacto;
    private TextView nombre, descripcion, movil, correo;
    private CircleImageView perfil;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_contact_profile);
        correo = findViewById(R.id.correo);
        nombre = findViewById(R.id.profileName);
        descripcion = findViewById(R.id.descripcion);
        movil = findViewById(R.id.Movile);
        perfil = findViewById(R.id.imageview_account_profile);
        contacto = getIntent().getParcelableExtra("personaActual");
        databaseReference = FirebaseDatabase.getInstance("https://sociallink-2bf20-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
                .child(contacto.getId());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {
                        contacto.setDescription(
                                Objects.requireNonNull(snapshot.child("descripcion").getValue()).toString());
                        contacto.setPhoneNumber(Integer.parseInt(Objects.requireNonNull(snapshot.child("numeroTelefono").getValue()).toString()));
                        contacto.setFotoPerfil(String.valueOf(Uri.parse(Objects.requireNonNull(snapshot.child("fotoPerfil").getValue()).toString())));
                        descripcion.setText(contacto.getDescription());
                        movil.setText(String.valueOf("Numero de contacto: " + contacto.getPhoneNumber()));


                    } catch (NullPointerException e) {
                        Toast.makeText(UserContactProfile.this, "Los datos de su contacto no están disponibles", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                Toast.makeText(UserContactProfile.this, "No hemos obtenido alguno de los datos de perfil, por favor comuniqueslo al equipo técnico", Toast.LENGTH_SHORT).show();
            }
        });


        nombre.setText(MessageFormat.format("Nick: {0}", contacto.getNombre()));
        descripcion.setText(R.string.default_description);
        movil.setText(MessageFormat.format("Numero de contacto: {0}", movil.getText()));
        correo.setText(MessageFormat.format("Correo: {0}", contacto.getEmail()));


        if (!(contacto.getFotoPerfil() == null || contacto.getFotoPerfil().equals(""))) {
            Glide.with(UserContactProfile.this)
                    .load(contacto.getFotoPerfil())
                    .fitCenter()
                    .centerCrop()
                    .into(perfil);
        } else {
            Glide.with(UserContactProfile.this)
                    .load(R.mipmap.ic_launcher)
                    .fitCenter()
                    .centerCrop()
                    .into(perfil);
        }
    }
}