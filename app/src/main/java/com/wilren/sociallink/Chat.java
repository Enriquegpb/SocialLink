package com.wilren.sociallink;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.wilren.sociallink.Adaptador.AdapterChatAuth;

import java.util.Date;

public class Chat extends AppCompatActivity {

    private RecyclerView rvMensajes;
    private LinearLayoutManager linearLayoutManager;
    private TextView NameUser;
    private EditText etMensaje;
    private ImageButton btnSend, btn;
    private AdapterChatAuth AdaptadorChats;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference chatreference = db.collection("chat");

    private void setComponents() {
        rvMensajes = findViewById(R.id.rvChat);
        etMensaje = findViewById(R.id.etMensajeChat);
        btnSend = findViewById(R.id.imageButton);

        Query query = FirebaseFirestore.getInstance()
                .collection("chat").orderBy("time", Query.Direction.ASCENDING);

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

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelChat chat = new ModelChat(user.getUid(), etMensaje.getText().toString(), new Date());
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
        setUserDatachat();


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
        btn = findViewById(R.id.userIdChat);
        NameUser.setText(user.getEmail());
        //btn.setImageURI();

    }

    /*private void openGallery(){
        Intent gallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, (PICK_IMAGE));
    }

    private void openCamera(){
        File fotoFile=new File(getApplicationContext().getFilesDir(),"fotoPerfil");
        String pathFotoFile=fotoFile.getAbsolutePath();
        Uri fotoUri=Uri.fromFile(fotoFile);
        Intent camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(camera.resolveActivity(getPackageManager()) != null){
            camera.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
            startActivityForResult(camera, (RESP_TOMAR_FOTO));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && (requestCode==PICK_IMAGE || requestCode==RESP_TOMAR_FOTO )){
            Uri imageUri = data.getData();
            ImageSwitcher imgPerfil_newuser_class=null;
            imgPerfil_newuser_class.setImageURI(imageUri);
            ImageSwitcher imgPerfil_toolbar_class=null;
            imgPerfil_toolbar_class.setImageURI(imageUri);


        }
    }*/
}