package com.wilren.sociallink;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.wilren.sociallink.Persona.Persona;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserContactProfile extends AppCompatActivity {

    private Persona contacto;
    private TextView nombre, descripcion, movil;
    private CircleImageView perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_contact_profile);

        contacto = getIntent().getParcelableExtra("personaActual");

        nombre = findViewById(R.id.profileName);
        descripcion = findViewById(R.id.descripcion);
        movil = findViewById(R.id.Movile);

        nombre.setText(contacto.getNombre());

        perfil = findViewById(R.id.imageview_account_profile);

        perfil.setImageURI(Uri.parse(contacto.getFotoPerfil()));


    }
}