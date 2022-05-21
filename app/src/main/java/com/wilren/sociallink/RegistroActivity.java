package com.wilren.sociallink;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wilren.sociallink.Persona.Persona;

import java.util.ArrayList;

public class RegistroActivity extends AppCompatActivity {

    private TextView username, usermail, userpassword;
    private Button accionRegistro;

    FirebaseAuth auth;
    FirebaseDatabase db;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        username = findViewById(R.id.userNameRegistro);
        usermail = findViewById(R.id.userEmailRegistro);
        userpassword = findViewById(R.id.passwordRegistro);
        accionRegistro = findViewById(R.id.accionRegistro);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://sociallink-2bf20-default-rtdb.europe-west1.firebasedatabase.app/");
        storage = FirebaseStorage.getInstance();

        accionRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user, email, pass;

                user = username.getText().toString();
                email = usermail.getText().toString();
                pass = userpassword.getText().toString();



                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){


                            DatabaseReference mDatabase = db.getReference().child("usuarios").child(auth.getUid());


                            Toast.makeText(RegistroActivity.this, "Se ha creado", Toast.LENGTH_SHORT).show();
                            //StorageReference storageReference = storage.getReference().child("subida").child(auth.getUid());

                        }else{
                            Toast.makeText(RegistroActivity.this, "No se ha creado nada", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


    }
}