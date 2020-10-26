package edu.bistu.gwclient.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import edu.bistu.gwclient.MainActivity;
import edu.bistu.gwclient.R;

public class RoomFragment extends Fragment
{
    private MainActivity master;

    private RecyclerView recyclerView_playerList;

    private Button button_returnHall;

    private Button button_prepare;

    private Button button_startGame;

    public RoomFragment(MainActivity master)
    {
        super();
        this.master = master;
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

        return view;
    }
}
