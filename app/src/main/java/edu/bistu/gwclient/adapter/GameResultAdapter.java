package edu.bistu.gwclient.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.bistu.gwclient.GameActivity;
import edu.bistu.gwclient.Memory;
import edu.bistu.gwclient.R;

public class GameResultAdapter extends RecyclerView.Adapter<GameResultAdapter.ViewHodler>
{
    private Long[][] results;

    public GameResultAdapter(Long[] arr)
    {
        results = new Long[arr.length / 2][2];
        for(int i = 0; i < results.length; i++)
        {
            results[i][0] = arr[i * 2];
            results[i][1] = arr[i * 2 + 1];
        }
    }

    class ViewHodler extends RecyclerView.ViewHolder
    {
        private TextView textView_username;
        private TextView textView_score;

        public ViewHodler(@NonNull View itemView)
        {
            super(itemView);
            textView_username = itemView.findViewById(R.id.textView_username);
            textView_score = itemView.findViewById(R.id.textView_score);
        }
    }

    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new ViewHodler(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position)
    {
        if(Memory.userCache.containsKey(results[position][0]))
            holder.textView_username.setText(Memory.userCache.get(results[position][0]).getUsername());
        else
            holder.textView_username.setText("uid" + results[position][0]);
        holder.textView_score.setText(Long.toString(results[position][1]));
    }

    @Override
    public int getItemCount()
    {
        return results.length;
    }
}
