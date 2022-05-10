package com.wilren.sociallink.AdaptadorMensajes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wilren.sociallink.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorMensaje extends RecyclerView.Adapter<AdaptadorMensaje.MensajesViewHolder>{
    private ArrayList <Mensaje> listaMensajes;

    public AdaptadorMensaje(ArrayList<Mensaje> listaMensajes){
        this.listaMensajes = listaMensajes;

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
        holder.ultMensaje.setText(listaMensajes.get(position).getUltMensaje());
        holder.fecha.setText(listaMensajes.get(position).getFecha());
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    public class MensajesViewHolder extends RecyclerView.ViewHolder{

        private TextView nombre, ultMensaje, fecha;
        private CircleImageView fotoPerfil;

        public MensajesViewHolder(@NonNull View view){
            super(view);
            nombre = view.findViewById(R.id.nombrePersona);
            ultMensaje = view.findViewById(R.id.ultimoMensaje);
            fecha = view.findViewById(R.id.fecha);
            fotoPerfil = view.findViewById(R.id.avatar);
        }
    }



}
