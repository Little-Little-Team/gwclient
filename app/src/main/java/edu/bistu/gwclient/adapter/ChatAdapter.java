package edu.bistu.gwclient.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.bistu.gwclient.GameActivity;
import edu.bistu.gwclient.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>
{
    private List<String> list;

    private GameActivity master;

    public ChatAdapter(GameActivity master)
    {
        this.master = master;
        list = new ArrayList<>();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textView_chat;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            textView_chat = itemView.findViewById(R.id.textView_chat);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.textView_chat.setText(list.get(position));
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public void addChat(String str)
    {
        list.add(str);
    }
}
