package com.wilren.sociallink;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MessengeHolder> {

    private List<ModelChat> listMessenges;

    public AdapterChat(List<ModelChat> listMessenges) {
        this.listMessenges = listMessenges;
    }


    @NonNull
    @Override
    public AdapterChat.MessengeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_chat, parent, false);
        return new MessengeHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterChat.MessengeHolder holder, int position) {
        holder.timeTv.setText(listMessenges.get(position).getTime());
        holder.textTv.setText(listMessenges.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return listMessenges.size();
    }

    public class MessengeHolder extends RecyclerView.ViewHolder {
        private TextView timeTv, textTv;


        public MessengeHolder(@NonNull View itemView) {
            super(itemView);
            timeTv=itemView.findViewById(R.id.idtime);
            textTv=itemView.findViewById(R.id.idtext);

        }
    }
}
