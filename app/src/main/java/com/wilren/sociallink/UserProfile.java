package com.wilren.sociallink;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wilren.sociallink.Persona.Persona;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.rxjava3.annotations.NonNull;

public class UserProfile extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private EditText et1, et2, et3;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private Button button;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private CircleImageView imageview_account_profile;
    private Uri imageUri;
    private Uri retrievePhotoProfile;
    private Persona personaActual;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        setContentView(R.layout.activity_user_profile);
        personaActual = getIntent().getParcelableExtra("personaActual");

        Toast.makeText(this, personaActual.getNombre(), Toast.LENGTH_SHORT).show();


        et1 = findViewById(R.id.profileName);
        et2 = findViewById(R.id.description);
        et3 = findViewById(R.id.Movile);

        et1.setText(user.getDisplayName());
        et2.setText(personaActual.getNombre());
        et3.setText(user.getEmail());



        button = findViewById(R.id.save);

        imageview_account_profile = findViewById(R.id.imageview_account_profile);

        imageview_account_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeUserPhoto(user);

            }
        });

        if (personaActual.getFotoPerfil() != null){
            Glide.with(UserProfile.this)
                    .load(personaActual.getFotoPerfil())
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
                personaMap.put("nombre", user.getDisplayName());
                personaMap.put("descripcion", et2.getText().toString());
                personaMap.put("numeroTelefono", et3.getText().toString());
                Toast.makeText(UserProfile.this, "xdxdxd", Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance("https://sociallink-2bf20-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
                        .child(user.getUid()).updateChildren(personaMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UserProfile.this, "true", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        Toast.makeText(UserProfile.this, "true", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
    /*private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }*/

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private void changeUserData(FirebaseUser user) {

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(et1.getText().toString())
                .build();


        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "User profile updated.");
                        }
                    }
                });
    }

    private void changeUserPhoto(FirebaseUser user) {
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
                        FirebaseDatabase.getInstance("https://sociallink-2bf20-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
                                .child(user.getUid()).updateChildren(personaMap2).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(UserProfile.this, "true", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@androidx.annotation.NonNull Exception e) {
                                Toast.makeText(UserProfile.this, "true", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Glide.with(UserProfile.this)
                                .load(retrievePhotoProfile)
                                .fitCenter()
                                .centerCrop()
                                .into(imageview_account_profile);
                        Toast.makeText(UserProfile.this, "La imagen se ha subido correctamente", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
}