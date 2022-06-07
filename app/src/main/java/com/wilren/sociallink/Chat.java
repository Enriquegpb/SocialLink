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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import com.squareup.picasso.Picasso;
import com.wilren.sociallink.AdaptadorMensajes.AdapterChatAuth;
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
                Intent intent = new Intent(Chat.this, UserContactProfile.class);
                intent.putExtra("personaActual", persona);
                startActivity(intent);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mensaje.length() > 0){
            if(mensaje.length() > 34){
                mensaje = mensaje.substring(0, 34) + "...";
            }
            ultimoMensaje();
            fechaActual();
        }

    }


    public void fechaActual(){
        FirebaseDatabase bbdd =  FirebaseDatabase.getInstance("https://sociallink-2bf20-default-rtdb.europe-west1.firebasedatabase.app/");

        DatabaseReference fb = bbdd.getReference("Contactos");
        fb.keepSynced(true);

        String tiempo = AdaptadorChats.fechaUltimoMensaje();
        fb.child(user.getUid()).child(persona.getId()).child("fecha").setValue(tiempo);
        fb.child(persona.getId()).child(user.getUid()).child("fecha").setValue(tiempo);
    }

    public void ultimoMensaje(){
        FirebaseDatabase bbdd =  FirebaseDatabase.getInstance("https://sociallink-2bf20-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference fb = bbdd.getReference("Contactos");
        fb.keepSynced(true);
        //Mensaje para persona actual
        fb.child(user.getUid()).child(persona.getId()).child("ultimoMensaje").setValue(mensaje);
        //Mensaje para persona a enviar
        fb.child(persona.getId()).child(user.getUid()).child("ultimoMensaje").setValue(mensaje);
    }

}