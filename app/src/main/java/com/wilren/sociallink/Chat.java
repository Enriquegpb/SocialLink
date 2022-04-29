package com.wilren.sociallink;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Chat extends AppCompatActivity {

    private RecyclerView rvMensajes;
    private EditText etMensaje;
    private ImageButton btnSend;

    private List<ModelChat> lstMensajes;
    private AdapterChat AdaptadorChats;

    private void setComponents() {
        rvMensajes = findViewById(R.id.rvChat);
        etMensaje = findViewById(R.id.etMensajeChat);
        btnSend = findViewById(R.id.imageButton);

        lstMensajes = new ArrayList<>();
        AdaptadorChats = new AdapterChat(lstMensajes);
        rvMensajes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvMensajes.setAdapter(AdaptadorChats);
        rvMensajes.setHasFixedSize(true);


        FirebaseFirestore.getInstance().collection("chat").addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("", "Error : " + e.getMessage());
                } else {
                    for (DocumentChange mDocumentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        if (mDocumentChange.getType() == DocumentChange.Type.ADDED) {
                            lstMensajes.add(mDocumentChange.getDocument().toObject(ModelChat.class));
                            AdaptadorChats.notifyDataSetChanged();
                            rvMensajes.smoothScrollToPosition(lstMensajes.size());
                        }
                    }
                }
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMensaje.length() == 0)
                    return;
                else {
                    ModelChat mensajeUser = new ModelChat();
                    mensajeUser.setUsername("Admin");
                    Date date=new Date();
                    mensajeUser.setTime(date.toString());
                    mensajeUser.setText(etMensaje.getText().toString());
                    FirebaseFirestore.getInstance().collection("chat").add(mensajeUser);
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
    }
}