package com.wilren.sociallink.Adaptador;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wilren.sociallink.Chat;
import com.wilren.sociallink.MainActivity;
import com.wilren.sociallink.Persona.Persona;
import com.wilren.sociallink.R;

import java.util.ArrayList;

public class AdaptadorMensaje extends RecyclerView.Adapter<AdaptadorMensaje.MensajesViewHolder> {
    private ArrayList<Persona> listaMensajes;
    private Activity activity;

    public AdaptadorMensaje(ArrayList<Persona> listaMensajes, Activity activity) {
        this.listaMensajes = listaMensajes;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MensajesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_mensajes, null, false);
        return new MensajesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MensajesViewHolder holder, int position) {
        holder.nombre.setText(listaMensajes.get(position).getNombre());
        holder.ultMensaje.setText(listaMensajes.get(position).getUltimoMensaje());
//        holder.fecha.setText(listaMensajes.get(position).getFecha());

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, Chat.class);
                intent.putExtra("personaEnviar", listaMensajes.get(position));
                intent.putExtra("nuevo", false);
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    public class MensajesViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, ultMensaje, fecha;
        View v;
        public MensajesViewHolder(@NonNull View view) {
            super(view);
            nombre = view.findViewById(R.id.nombrePersona);
            ultMensaje = view.findViewById(R.id.ultimoMensaje);
            fecha = view.findViewById(R.id.fecha);
            v = view;
        }
    }



}
