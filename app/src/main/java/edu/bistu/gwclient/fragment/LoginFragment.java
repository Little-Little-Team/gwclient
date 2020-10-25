package edu.bistu.gwclient.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.bistu.gwclient.LoginActivity;
import edu.bistu.gwclient.R;

public class LoginFragment extends Fragment
{
    private LoginActivity master;

    private EditText editText_username;
    private EditText editText_pw;
    private TextView textView_hint;
    private Button button_login;
    private TextView textView_toRegister;

    public LoginFragment(LoginActivity master)
    {
        this.master = master;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editText_username = view.findViewById(R.id.editText_username);
        editText_pw = view.findViewById(R.id.editText_pw);
        textView_hint = view.findViewById(R.id.textView_hint);
        button_login = view.findViewById(R.id.button_login_or_register);
        textView_toRegister = view.findViewById(R.id.textView_toRegister);

        button_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                master.onLoginButtonClicked(editText_username.getText().toString(), editText_pw.getText().toString());
            }
        });

        textView_toRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                master.toRegister();
            }
        });
        return view;
    }

    public void setHintContent(String content)
    {
        textView_hint.setText(content);
        textView_hint.setVisibility(View.VISIBLE);
    }

}
