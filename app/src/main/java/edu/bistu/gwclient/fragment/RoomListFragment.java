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

public class RoomListFragment extends Fragment
{
    private MainActivity master;

    private RecyclerView recyclerView_roomList;

    private Button button_returnHall;

    public RoomListFragment(MainActivity master)
    {
        super();
        this.master = master;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_roomlist, container, false);

        recyclerView_roomList = view.findViewById(R.id.recyclerView_roomList);
        button_returnHall = view.findViewById(R.id.button_returnHall);

        button_returnHall.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });

        return view;
    }
}
