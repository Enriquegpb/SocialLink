package com.wilren.sociallink;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wilren.sociallink.Persona.Persona;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LoginTabFragment extends Fragment {

    private Button blogin;
    private EditText email, password;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private ArrayList <Persona> listaPersonas;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        blogin = root.findViewById(R.id.blogin);
        email = root.findViewById(R.id.editTextTextEmail);
        password = root.findViewById(R.id.editTextTextPassword);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://sociallink-2bf20-default-rtdb.europe-west1.firebasedatabase.app/");

        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        return root;

    }

    public void login() {
        String mail = "persona3@gmail.com";
        String pass = "123456";
        ArrayList <String> listaC = new ArrayList<>();

        if (mail.isEmpty()) {
            email.setError("");
        }
        if (pass.isEmpty()) {
            password.setError("");
        } else {
            mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Persona persona = null;
                        Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();

                        //Intent logeado = new Intent(getContext(), MainActivity.class);

                        DatabaseReference contactos = db.getReference("Contactos").child(mAuth.getUid());
                        contactos.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    listaC.add(dataSnapshot.getKey());
                                }
                                Intent logeado = new Intent(getActivity(), MainActivity.class);
                                logeado.putStringArrayListExtra("con", listaC);
                                startActivity(logeado);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else {
                        Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private String nombreUsuario(String mail) {
        return mail.substring(0, 8);
    }

}
