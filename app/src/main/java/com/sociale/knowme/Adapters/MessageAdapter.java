package com.sociale.knowme.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.sociale.knowme.Models.AllMethods;
import com.sociale.knowme.Models.Message;
import com.sociale.knowme.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder>{
    Context context;
    List<Message> messages;
    DatabaseReference msgdb;

    public MessageAdapter(Context context , List<Message> messages, DatabaseReference msgDb){
        this.context = context;
        this.msgdb = msgDb;
        this.messages = messages;

    }
    @NonNull
    @Override
    public MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message,parent,false);
        return new MessageAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MessageAdapterViewHolder holder, int position) {
        Message msg =messages.get(position);
        if(msg.getName().equals(AllMethods.name)){
            holder.tvTitle.setText("You: "+msg.getMessage());
            holder.tvTitle.setGravity(Gravity.START);
            holder.L1.setBackgroundColor(Color.parseColor("#EF9E73"));

        }else{
            holder.tvTitle.setText(msg.getName()+": "+msg.getMessage());
            holder.ibDelete.setVisibility(View.GONE);
        }

    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        ImageButton ibDelete;
        LinearLayout L1;
         public MessageAdapterViewHolder(View itemView){
             super(itemView);
             tvTitle = itemView.findViewById(R.id.textview);
             ibDelete = itemView.findViewById(R.id.delete);
             L1 = itemView.findViewById(R.id.L1msg);
             ibDelete.setOnClickListener(new View.OnClickListener() {
                 /**
                  * Called when a view has been clicked.
                  *
                  * @param v The view that was clicked.
                  */
                 @Override
                 public void onClick(View v) {
                     msgdb.child(messages.get(getAbsoluteAdapterPosition()).getKey()).removeValue();
                 }
             });
         }
    }
}
