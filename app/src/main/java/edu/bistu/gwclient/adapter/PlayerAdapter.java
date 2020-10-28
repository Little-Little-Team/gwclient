package edu.bistu.gwclient.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.Set;

import edu.bistu.gwclient.MainActivity;
import edu.bistu.gwclient.Memory;
import edu.bistu.gwclient.R;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder>
{
    private Long[][] players;

    private MainActivity master;

    private Set<Long> set;

    private int count;

    public PlayerAdapter(MainActivity master)
    {
        players = new Long[Memory.maxRoomSize][2];
        this.master = master;
        set = new HashSet<>();
        count = 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textView_isReady;
        private ImageView imageView_avatar;
        private TextView textView_username;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            textView_isReady = itemView.findViewById(R.id.textView_isReady);
            imageView_avatar = itemView.findViewById(R.id.imageView_avatar);
            textView_username = itemView.findViewById(R.id.textView_item_result_username);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Long[] arr = players[position];
        Log.d(getClass().getName(), "position " + position + " bind view holder: " + arr[0] + ",  " + arr[1]);
        if(arr[0] == null)
        {
            holder.textView_isReady.setText("");
            holder.imageView_avatar.setImageResource(R.drawable.add);
            holder.textView_username.setText("");
        }
        else
        {
            if(arr[1] == 1)
                holder.textView_isReady.setText("已准备");
            else
                holder.textView_isReady.setText("未准备");

            holder.imageView_avatar.setImageResource(R.drawable.boy_icon);

            master.setUserPropertyInBackground(arr[0], holder.textView_username, holder.imageView_avatar);
        }
    }

    @Override
    public int getItemCount()
    {
        return players.length;
    }

    public void updateData(Long[] arr)
    {
        count = arr.length / 2;
        Log.d(getClass().getName(), "count = " + count);
        for(int i = 0; i < players.length; i++)
        {
            if(i * 2 + 1 < arr.length)
            {
                players[i][0] = arr[i * 2];
                players[i][1] = arr[i * 2 + 1];

                if(players[i][1] == 1)
                {
                    Log.d(getClass().getName(), "set.add()");
                    set.add(players[i][0]);
                }
                else
                {
                    Log.d(getClass().getName(), "set.remove()");
                    set.remove(players[i][0]);
                }
            }
            else
            {
                players[i][0] = null;
                players[i][1] = null;
            }
        }
    }

    public boolean isRoomOwner()
    {
        return players[0][0] != null && players[0][0].equals(Memory.id);
    }

    public boolean canStartGame()
    {
        Log.d(getClass().getName(), "set.size(): " + set.size());
        if(set.size() == count)
            return true;
        else
            return false;
    }
}
