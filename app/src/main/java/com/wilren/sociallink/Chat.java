package com.wilren.sociallink;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

import java.util.Date;

public class Chat extends AppCompatActivity {

    private RecyclerView rvMensajes;
    private TextView NameUser;
    private EditText etMensaje;
    private ImageButton btnSend,btn;
    private AdapterChatAuth AdaptadorChats;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference chatreference = db.collection("chat");

    private void setComponents() {
        rvMensajes = findViewById(R.id.rvChat);
        etMensaje = findViewById(R.id.etMensajeChat);
        btnSend = findViewById(R.id.imageButton);

        Query query = FirebaseFirestore.getInstance()
                .collection("chat").orderBy("time", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ModelChat> options = new FirestoreRecyclerOptions.Builder<ModelChat>().setQuery(query, ModelChat.class).build();

        AdaptadorChats = new AdapterChatAuth(options);
        AdaptadorChats.registerAdapterDataObserver(new
                                                           RecyclerView.AdapterDataObserver() {
                                                               @Override
                                                               public void onItemRangeChanged(int positionStart, int itemCount) {
                                                                   rvMensajes.smoothScrollToPosition(AdaptadorChats.getItemCount() - 1);
                                                               }
                                                           });
        rvMensajes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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


         /*btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                // setProfileSettings();
             }
         });*/
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

    /*public void setProfileSettings(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName("Jane Q. User")
                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
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
    }*/

    public void setUserDatachat(){
        NameUser=findViewById(R.id.userNameChat);
        btn= findViewById(R.id.userIdChat);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        NameUser.setText(user.getEmail());
        //btn.setImageURI();

    }
}