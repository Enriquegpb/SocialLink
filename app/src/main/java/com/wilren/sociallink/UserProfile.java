package com.wilren.sociallink;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wilren.sociallink.Persona.Persona;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.rxjava3.annotations.NonNull;

public class UserProfile extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private EditText et1, et2, et3;
    private Button button;
    private CircleImageView imageview_account_profile;
    private Uri retrievePhotoProfile, imageUri;
    private Persona personaActual;
    private DatabaseReference databaseReference;
    private TextView descripcionTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        databaseReference = FirebaseDatabase.getInstance("https://sociallink-2bf20-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
                .child(user.getUid());
        et1 = findViewById(R.id.profileName);
        et2 = findViewById(R.id.description);
        et3 = findViewById(R.id.Movile);
        descripcionTv=findViewById(R.id.descripcion);
        et1.setText(user.getDisplayName());


        personaActual = getIntent().getParcelableExtra("personaActual");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {
                        personaActual.setDescription(
                                Objects.requireNonNull(snapshot.child("descripcion").getValue()).toString());
                        personaActual.setPhoneNumber(Integer.parseInt(Objects.requireNonNull(snapshot.child("numeroTelefono").getValue()).toString()));
                        et2.setText(personaActual.getDescription());
                        descripcionTv.setText(personaActual.getDescription());
                        et3.setText(String.valueOf(personaActual.getPhoneNumber()));
                    } catch (NullPointerException e) {
                        Toast.makeText(UserProfile.this, "Adelante usuario. Puedes editar tu perfil.", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                Toast.makeText(UserProfile.this, "No hemos obtenido alguno de los datos de perfil, por favor comuniqueslo al equipo técnico", Toast.LENGTH_SHORT).show();
            }
        });


        button = findViewById(R.id.save);
        imageview_account_profile = findViewById(R.id.imageview_account_profile);

        imageview_account_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeUserPhoto();


            }
        });

        if (!(personaActual.getFotoPerfil() == null || personaActual.getFotoPerfil().equals(""))) {
            Glide.with(UserProfile.this)
                    .load(personaActual.getFotoPerfil())
                    .fitCenter()
                    .centerCrop()
                    .into(imageview_account_profile);
        } else {
            Glide.with(UserProfile.this)
                    .load(R.mipmap.ic_launcher)
                    .fitCenter()
                    .centerCrop()
                    .into(imageview_account_profile);
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeUserData(user);
                et1.setText(et1.getText());
                et2.setText(et2.getText());
                et3.setText(et3.getText());


                Map<String, Object> personaMap = new HashMap<>();
                if (Objects.requireNonNull(user.getDisplayName()).length() > 0)
                    personaMap.put("nombre", user.getDisplayName());
                else
                    personaMap.put("nombre", "");
                personaMap.put("descripcion", et2.getText().toString());
                personaMap.put("numeroTelefono", et3.getText().toString());
                databaseReference.updateChildren(personaMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UserProfile.this, "La actualizacion de los datos del perfil se ha realizado correctamente", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        Toast.makeText(UserProfile.this, "La actualización de los datos del perfil de usuario ha fallado", Toast.LENGTH_SHORT).show();
                    }
                });
                salir(view, personaActual);
            }
        });

    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private void changeUserData(FirebaseUser user) {
        if (et1.getText().length() > 0) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(et1.getText().toString())
                    .build();


            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@androidx.annotation.NonNull @NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "User profile updated.");
                            }
                        }
                    });
        }

    }

    private void changeUserPhoto() {
        openGallery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK && (requestCode == PICK_IMAGE)) {
            imageUri = data.getData();

            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("fotos").child(imageUri.getLastPathSegment());
            UploadTask uploadTask = filePath.putFile(imageUri);
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@androidx.annotation.NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@androidx.annotation.NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        retrievePhotoProfile = task.getResult();
                        Map<String, Object> personaMap2 = new HashMap<>();
                        personaMap2.put("fotoPerfil", retrievePhotoProfile.toString());
                        databaseReference.updateChildren(personaMap2).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(UserProfile.this, "La imagen se ha subido correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@androidx.annotation.NonNull Exception e) {
                                Toast.makeText(UserProfile.this, "No se podido ha subir la foto de perfil", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Glide.with(UserProfile.this)
                                .load(retrievePhotoProfile)
                                .fitCenter()
                                .centerCrop()
                                .into(imageview_account_profile);
                        Toast.makeText(UserProfile.this, "Se ha cambiado la foto de perfil", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        setResult(RESULT_CANCELED, null);
    }

    public void salir(View vista, Persona persona_return) {
        Intent my_resultado = new Intent();
        my_resultado.putExtra("persona_return", persona_return);
        setResult(RESULT_OK, my_resultado);
        this.finish();
    }
}