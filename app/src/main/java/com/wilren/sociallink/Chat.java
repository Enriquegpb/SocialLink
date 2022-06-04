package com.wilren.sociallink;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;
import com.wilren.sociallink.Adaptador.AdapterChatAuth;
import com.wilren.sociallink.Persona.Persona;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

    private Persona persona;
    private final int RESP_TOMAR_FOTO = 0;
    private final int PICK_IMAGE = 1;
    private RecyclerView rvMensajes;
    private LinearLayoutManager linearLayoutManager;
    private TextView userName;
    private EditText etMensaje;
    private ImageButton btnSend;
    private CircleImageView perfil;
    private AdapterChatAuth AdaptadorChats;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference chatreference = db.collection("chat");
    private CollectionReference chatEnviar = chatreference;
    private Uri imageUri;
    private String mensaje = "";
    private boolean nuevo;

    private void setComponents() {

        nuevo = getIntent().getExtras().getBoolean("nuevo");
        persona = getIntent().getParcelableExtra("personaEnviar");

        chatreference = chatreference.document(user.getUid()).collection(persona.getId());
        chatEnviar = chatEnviar.document(persona.getId()).collection(user.getUid());

        rvMensajes = findViewById(R.id.rvChat);
        etMensaje = findViewById(R.id.etMensajeChat);
        btnSend = findViewById(R.id.imageButton);

        Query query = db.collection("chat").document(user.getUid()).collection(persona.id)
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
                mensaje = etMensaje.getText().toString();
                if(!mensaje.isEmpty()){
                    ModelChat chat = new ModelChat(user.getUid(), mensaje, new Date());
                    chatreference.add(chat);
                    chatEnviar.add(chat);
                    etMensaje.setText("");
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setComponents();
        setUserDatachat();

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                changephoto(view);
                Intent intent = new Intent(Chat.this, UserContactProfile.class);
                intent.putExtra("personaActual",persona);
                startActivity(intent);
                Toast.makeText(Chat.this, "Futura informacion del usuario.", Toast.LENGTH_SHORT).show();
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
        userName = findViewById(R.id.userNameChat);
        userName.setText(persona.getNombre());
        perfil = findViewById(R.id.avatarUsuario);

        if(!persona.getFotoPerfil().isEmpty() && persona.getFotoPerfil().length() > 0){
            Picasso.get().load(persona.getFotoPerfil()).placeholder(R.drawable.user).into(perfil);
        }
    }


//    //hasta aqui
//    public void changephoto(View view) {
//
//        openGallery();
//
//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setPhotoUri(Uri.parse(String.valueOf(imageUri)))
//                .build();
//
//        user.updateProfile(profileUpdates)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d("TAG", "User profile updated.");
//                        }
//                    }
//                });
//        AdaptadorChats.notifyDataSetChanged();
//    }
//
//    private void openGallery() {
//        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        startActivityForResult(gallery, PICK_IMAGE);
//    }
//
//    private void openCamera() {
//        File fotoFile = new File(getApplicationContext().getFilesDir(), "fotoPerfil");
//        String pathFotoFile = fotoFile.getAbsolutePath();
//        Uri fotoUri = Uri.fromFile(fotoFile);
//        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (camera.resolveActivity(getPackageManager()) != null) {
//            camera.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
//            startActivityForResult(camera, RESP_TOMAR_FOTO);
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK && (requestCode == PICK_IMAGE || requestCode == RESP_TOMAR_FOTO)) {
//            imageUri = data.getData();
//            //imgPerfil_newuser_class.setImageURI(imageUri);
//            //imgPerfil_toolbar_class.setImageURI(imageUri);
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(nuevo && mensaje.length() > 0){
            ultimoMensaje();
        }
    }

    public void ultimoMensaje(){
        FirebaseDatabase.getInstance("https://sociallink-2bf20-default-rtdb.europe-west1.firebasedatabase.app/").
                getReference("Contactos").
                child(user.getUid()).
                child(persona.getId()).setValue("");

        FirebaseDatabase.getInstance("https://sociallink-2bf20-default-rtdb.europe-west1.firebasedatabase.app/").
                getReference("Contactos").
                child(persona.getId()).
                child(user.getUid()).setValue("");

    }
}