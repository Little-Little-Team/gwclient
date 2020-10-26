package edu.bistu.gwclient.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.bistu.gwclient.MainActivity;
import edu.bistu.gwclient.R;
import edu.bistu.gwclient.adapter.PlayerAdapter;

public class RoomFragment extends Fragment
{
    private MainActivity master;

    private RecyclerView recyclerView_playerList;

    private Button button_returnHall;

    private Button button_prepare;

    private Button button_startGame;

    private Integer roomID;

    private PlayerAdapter adapter;

    public RoomFragment(MainActivity master, Integer roomID)
    {
        super();
        this.master = master;
        this.roomID = roomID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_room, container, false);

        recyclerView_playerList = view.findViewById(R.id.recyclerView_playerList);
        button_returnHall = view.findViewById(R.id.button_returnHall);
        button_prepare = view.findViewById(R.id.button_prepare);
        button_startGame = view.findViewById(R.id.button_startGame);

        button_returnHall.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });

        button_prepare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });

        button_startGame.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(adapter.isRoomOwner())
                {

                }
                else
                    Toast.makeText(RoomFragment.this.getContext(), "你不是房主，不能开始游戏", Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new PlayerAdapter(master);
        recyclerView_playerList.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        recyclerView_playerList.setAdapter(adapter);

        return view;
    }

    public void setRoomID(Integer roomID)
    {
        this.roomID = roomID;
    }

    public void updateData(Long[] arr)
    {
        adapter.updateData(arr);
        adapter.notifyDataSetChanged();
    }
}
