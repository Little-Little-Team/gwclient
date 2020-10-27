package edu.bistu.gwclient.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.bistu.gwclient.GameActivity;
import edu.bistu.gwclient.R;
import edu.bistu.gwclient.adapter.GameResultAdapter;

public class GameResultDialogFragment extends DialogFragment
{
    private GameActivity master;

    private RecyclerView recyclerView_result;

    private GameResultAdapter adapter;

    private Button button_confirm;

    public GameResultDialogFragment(GameActivity master, Long[] arr)
    {
        this.master = master;
        adapter = new GameResultAdapter(arr);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.dialog_game_result, container, false);

        recyclerView_result = view.findViewById(R.id.recyclerView_result);
        button_confirm = view.findViewById(R.id.button_confirm);

        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView_result.setAdapter(adapter);

        button_confirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                master.onConfirmClicked();
            }
        });

        return view;
    }
}
