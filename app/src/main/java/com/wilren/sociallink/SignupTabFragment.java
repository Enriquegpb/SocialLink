package com.wilren.sociallink;

import android.content.Intent;
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

public class SignupTabFragment extends Fragment {

    private Button bsignup;
    private EditText username, email, password, repeatPassword;
    private FirebaseAuth mAuth;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);

        bsignup = root.findViewById(R.id.bsignup);
        username = root.findViewById(R.id.editTextTextPersonName);
        email = root.findViewById(R.id.editTextTextEmail);
        password = root.findViewById(R.id.editTextTextPassword);
        repeatPassword = root.findViewById(R.id.editTextRepeatPassword);
        mAuth = FirebaseAuth.getInstance();

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
            username.setError("");
        }
        if (mail.isEmpty()) {
            email.setError("");
        }
        if (pass.isEmpty()) {
            password.setError("");
        }
        if (repeatPass.isEmpty()) {
            repeatPassword.setError("");
        } else {
            mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Successfully registered", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), Chat.class));
                    } else {
                        Toast.makeText(getActivity(), "Oops, something went wrong, try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
