package com.wilren.sociallink;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.wilren.sociallink.Adaptador.AdapterChatAuth;
import com.wilren.sociallink.Persona.Persona;

import java.io.File;
import java.util.Date;

import io.reactivex.rxjava3.annotations.NonNull;

public class Chat extends AppCompatActivity {
    private Persona persona;
    private static final int RESP_TOMAR_FOTO = 0;
    private static final int PICK_IMAGE = 1;
    private RecyclerView rvMensajes;
    private LinearLayoutManager linearLayoutManager;
    private TextView NameUser;
    private EditText etMensaje;
    private ImageButton btnSend, btn;
    private AdapterChatAuth AdaptadorChats;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference chatreference = db.collection(user.getUid());
    private Uri imageUri;

    private void setComponents() {
        persona = getIntent().getParcelableExtra("personaEnviar");
        chatreference = chatreference.document(persona.getId() + "mensajes").collection("mensajes");

        rvMensajes = findViewById(R.id.rvChat);
        etMensaje = findViewById(R.id.etMensajeChat);
        btnSend = findViewById(R.id.imageButton);

        Query query = FirebaseFirestore.getInstance()
                .collection(user.getUid()).document(persona.getId() + "mensajes").collection("mensajes")
                .orderBy("time", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ModelChat> options = new FirestoreRecyclerOptions.Builder<ModelChat>().setQuery(query, ModelChat.class).build();

        AdaptadorChats = new AdapterChatAuth(options);
        AdaptadorChats.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                rvMensajes.smoothScrollToPosition(AdaptadorChats.getItemCount() - 1);
            }
        });
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        rvMensajes.setLayoutManager(linearLayoutManager);
        rvMensajes.setAdapter(AdaptadorChats);
        rvMensajes.setHasFixedSize(true);
        rvMensajes.setItemAnimator(null);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelChat chat = new ModelChat(user.getUid(), etMensaje.getText().toString(), user.getPhotoUrl().toString(), new Date());
                chatreference.add(chat);
                etMensaje.setText("");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setComponents();
        btn = findViewById(R.id.userIdChat);
        setUserDatachat();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changephoto(view);
                btn.setImageURI(user.getPhotoUrl());
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        AdaptadorChats.startListening();
    }

    protected void onStop() {
        super.onStop();
        AdaptadorChats.stopListening();
    }

    public void setUserDatachat() {
        NameUser = findViewById(R.id.userNameChat);
        NameUser.setText(user.getEmail());


    }

    public void changephoto(View view) {

        openGallery();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(String.valueOf(imageUri)))
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
        AdaptadorChats.notifyDataSetChanged();
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private void openCamera() {
        File fotoFile = new File(getApplicationContext().getFilesDir(), "fotoPerfil");
        String pathFotoFile = fotoFile.getAbsolutePath();
        Uri fotoUri = Uri.fromFile(fotoFile);
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (camera.resolveActivity(getPackageManager()) != null) {
            camera.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
            startActivityForResult(camera, RESP_TOMAR_FOTO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && (requestCode == PICK_IMAGE || requestCode == RESP_TOMAR_FOTO)) {
            imageUri = data.getData();
            //imgPerfil_newuser_class.setImageURI(imageUri);
            //imgPerfil_toolbar_class.setImageURI(imageUri);
        }
    }
}