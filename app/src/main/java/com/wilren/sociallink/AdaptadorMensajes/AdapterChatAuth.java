package com.wilren.sociallink.Adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wilren.sociallink.ModelChat;
import com.wilren.sociallink.R;

import org.ocpsoft.prettytime.PrettyTime;

public class AdapterChatAuth extends FirestoreRecyclerAdapter<ModelChat, AdapterChatAuth.MessengeHolder> {
    private static final int M_R = 0;
    private static final int M_I = 1;

    PrettyTime p = new PrettyTime();


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterChatAuth(@NonNull FirestoreRecyclerOptions<ModelChat> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MessengeHolder holder, int position, @NonNull ModelChat model) {
//        Una forma mas bonita de hacer la fecha
//        Date fecha = model.getTime();
//        String tiempo = fecha.getHours() +":"+ fecha.getMinutes() + "";
        holder.timeTv.setText(p.format(model.getTime()));
        holder.textTv.setText(model.getText());
    }

    @NonNull
    @Override
    public MessengeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == M_R)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_chat_auth, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_chat_user_link, parent, false);

        return new MessengeHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getUserid().equals(user.getUid()))
            return M_R;
        else
            return M_I;
    }

    public class MessengeHolder extends RecyclerView.ViewHolder {
        private final TextView timeTv, textTv;

        public MessengeHolder(@NonNull View itemView) {
            super(itemView);
            timeTv = itemView.findViewById(R.id.idtime);
            textTv = itemView.findViewById(R.id.idtext);
        }
    }
}
