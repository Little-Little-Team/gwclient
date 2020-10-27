package edu.bistu.gwclient.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.bistu.gwclient.MainActivity;
import edu.bistu.gwclient.R;

public class HallFragment extends Fragment
{
    private MainActivity master;

    private Button button_quickJoin;

    private Button button_newRoom;

    private Button button_roomList;

    public HallFragment(MainActivity master)
    {
        super();
        this.master = master;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_hall, container, false);

        button_quickJoin = view.findViewById(R.id.button_quickJoin);
        button_newRoom = view.findViewById(R.id.button_newRoom);
        button_roomList = view.findViewById(R.id.button_roomList);

        button_quickJoin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                master.onQuickJoinClicked();
            }
        });

        button_newRoom.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                master.onCreateRoomClicked();
            }
        });

        button_roomList.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                master.onCreateRoomClicked();
            }
        });

        return view;
    }
}
