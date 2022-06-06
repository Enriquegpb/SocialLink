package com.wilren.sociallink.Adaptador;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.wilren.sociallink.Chat;
import com.wilren.sociallink.MainActivity;
import com.wilren.sociallink.ModelChat;
import com.wilren.sociallink.Persona.Persona;
import com.wilren.sociallink.R;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorMensaje extends RecyclerView.Adapter<AdaptadorMensaje.MensajesViewHolder> {
    private ArrayList<Persona> listaMensajes;
    private Activity activity;
    private String persona;
    private CollectionReference chatreference;

    public AdaptadorMensaje(ArrayList<Persona> listaMensajes, Activity activity, String persona) {
        this.listaMensajes = listaMensajes;
        this.activity = activity;
        this.persona = persona;
    }

    @NonNull
    @Override
    public MensajesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_mensajes, null, false);
        return new MensajesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MensajesViewHolder holder, int position) {
        Persona persona = listaMensajes.get(position);

        holder.nombre.setText(persona.getNombre());
        holder.ultMensaje.setText(persona.getUltimoMensaje());

        if(!persona.getFotoPerfil().isEmpty()){
            Picasso.get().load(persona.getFotoPerfil()).placeholder(R.drawable.user).into(holder.fotoPerfil);
        }

        holder.fecha.setText(persona.getFechaUltimoMensaje());

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, Chat.class);
                intent.putExtra("personaEnviar",persona);
                intent.putExtra("nuevo", false);
                activity.startActivity(intent);
            }
        });

        holder.v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Borrar conversacion");
                builder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        borrarConversacion(listaMensajes.get(position).getId());
                    }
                });

                builder.setNegativeButton("Cancelar", null);
                builder.create().show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    public class MensajesViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView fotoPerfil;
        private TextView nombre, ultMensaje, fecha;
        View v;
        public MensajesViewHolder(@NonNull View view) {
            super(view);
            nombre = view.findViewById(R.id.nombrePersona);
            ultMensaje = view.findViewById(R.id.ultimoMensaje);
            fecha = view.findViewById(R.id.fecha);
            fotoPerfil = view.findViewById(R.id.avatar);
            v = view;
        }
    }

    public void borrarConversacion(String id){
        chatreference = FirebaseFirestore.getInstance().collection("chat");
        chatreference = chatreference.document(persona).collection(id);

        chatreference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        chatreference.document(document.getId()).delete();
                    }
                }
            }
        });

        Toast.makeText(activity, "conversacion borrada", Toast.LENGTH_SHORT).show();
    }


}
