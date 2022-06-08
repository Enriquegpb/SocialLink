package com.wilren.sociallink.Fragments;

import android.os.Bundle;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wilren.sociallink.Persona.Persona;
import com.wilren.sociallink.R;

public class SignupTabFragment extends Fragment {

    private Button bsignup;
    private EditText username, email, password, repeatPassword;
    private FirebaseAuth mAuth;
    private FirebaseDatabase fbdb;
    private FirebaseFirestore db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);

        bsignup = root.findViewById(R.id.bsignup);
        username = root.findViewById(R.id.userRegistro);
        email = root.findViewById(R.id.emailRegistro);
        password = root.findViewById(R.id.passwordRegistro);
        repeatPassword = root.findViewById(R.id.passwordConfirmacionRegistro);
        mAuth = FirebaseAuth.getInstance();
        fbdb = FirebaseDatabase.getInstance("https://sociallink-2bf20-default-rtdb.europe-west1.firebasedatabase.app/");
        db = FirebaseFirestore.getInstance();

        bsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
        return root;
    }

    private void signup() {
        String user = username.getText().toString().trim();
        String mail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String repeatPass = repeatPassword.getText().toString().trim();
        if (user.isEmpty()) {
            username.setError("Introduce el nombre de usuario");
        }
        if (mail.isEmpty()) {
            email.setError("Introduce el correo eletrónico");
        }
        if (pass.isEmpty()) {
            password.setError("Debes introducir una contraseña");
        }
        if (repeatPass.isEmpty()) {
            repeatPassword.setError("Debes repetir la contraseña");
        } else {
            mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Te has registrado en la aplicación", Toast.LENGTH_SHORT).show();
                        String id = task.getResult().getUser().getUid();
                        Persona persona = new Persona(id, user, mail, "", "", 0);
                        fbdb.getReference().child("Users").child(id).setValue(persona);
                        fbdb.getReference().child("Contactos").child(id).setValue("");
                    } else {
                        Toast.makeText(getActivity(), "Error de registro de usuario, inténtelo otra vez", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
