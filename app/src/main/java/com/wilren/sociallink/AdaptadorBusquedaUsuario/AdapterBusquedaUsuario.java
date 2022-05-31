package com.wilren.sociallink.AdaptadorBusquedaUsuario;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wilren.sociallink.Chat;
import com.wilren.sociallink.Persona.Persona;
import com.wilren.sociallink.R;

import org.w3c.dom.Text;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;

public class AdapterBusquedaUsuario extends RecyclerView.Adapter<AdapterBusquedaUsuario.AdapterBusquedaUsuarioViewHolder> {
    private ArrayList<Persona> listaPersonas;
    private Activity activity;

    public AdapterBusquedaUsuario(ArrayList listaPersonas, Activity activity){
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
        holder.nombre.setText(listaPersonas.get(position).getNombre());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, Chat.class);
                intent.putExtra("personaEnviar", listaPersonas.get(position));
                intent.putExtra("nuevo", true);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPersonas.size();
    }

    public class AdapterBusquedaUsuarioViewHolder extends RecyclerView.ViewHolder{
        TextView nombre;
        View view;
        public AdapterBusquedaUsuarioViewHolder(@NonNull View v) {
            super(v);
            nombre = v.findViewById(R.id.nombrePersona);
            view = v;
        }
    }
}
