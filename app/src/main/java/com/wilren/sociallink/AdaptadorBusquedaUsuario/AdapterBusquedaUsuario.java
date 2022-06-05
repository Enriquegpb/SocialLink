package com.wilren.sociallink.AdaptadorBusquedaUsuario;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wilren.sociallink.Chat;
import com.wilren.sociallink.Persona.Persona;
import com.wilren.sociallink.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterBusquedaUsuario extends RecyclerView.Adapter<AdapterBusquedaUsuario.AdapterBusquedaUsuarioViewHolder> {
    private ArrayList<Persona> listaPersonas;
    private Activity activity;

    public AdapterBusquedaUsuario(ArrayList listaPersonas, Activity activity) {
        this.listaPersonas = listaPersonas;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterBusquedaUsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_busqueda_usuario, null, false);
        return new AdapterBusquedaUsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBusquedaUsuarioViewHolder holder, int position) {
        Persona persona = listaPersonas.get(position);
        holder.nombre.setText(persona.getNombre());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, Chat.class);
                intent.putExtra("personaEnviar", persona);
                intent.putExtra("nuevo", true);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        if (!persona.getFotoPerfil().isEmpty()) {
            Picasso.get().load(persona.getFotoPerfil()).placeholder(R.drawable.user).into(holder.fotoPerfil);
        }

    }

    @Override
    public int getItemCount() {
        return listaPersonas.size();
    }

    public class AdapterBusquedaUsuarioViewHolder extends RecyclerView.ViewHolder {
        private TextView nombre;
        private CircleImageView fotoPerfil;
        private View view;

        public AdapterBusquedaUsuarioViewHolder(@NonNull View v) {
            super(v);
            nombre = v.findViewById(R.id.nombrePersona);
            fotoPerfil = v.findViewById(R.id.fotoPerfil);
            view = v;
        }
    }

    public void setFilteredList(ArrayList listaPersonasBusqueda) {
        this.listaPersonas = listaPersonasBusqueda;
        notifyDataSetChanged();
    }
}
