package com.wilren.sociallink.Fragments;

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
import com.wilren.sociallink.MainActivity;
import com.wilren.sociallink.Persona.Persona;
import com.wilren.sociallink.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LoginTabFragment extends Fragment {

    private Button blogin;
    private EditText email, password;
    private FirebaseAuth mAuth;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        blogin = root.findViewById(R.id.blogin);
        email = root.findViewById(R.id.editTextTextEmail);
        password = root.findViewById(R.id.editTextTextPassword);
        mAuth = FirebaseAuth.getInstance();

        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        return root;
    }

    public void login() {
        String mail = "persona2@gmail.com";
        String pass = "123456";

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
                        Intent logeado = new Intent(getActivity(), MainActivity.class);
                        startActivity(logeado);
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
