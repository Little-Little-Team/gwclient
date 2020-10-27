package edu.bistu.gwclient.fragment;

import android.os.Bundle;
import android.os.Message;
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
import edu.bistu.gwclient.Memory;
import edu.bistu.gwclient.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterFragment extends Fragment
{
    private LoginActivity master;

    private EditText editText_username;
    private EditText editText_pw;
    private EditText editText_pw2;
    private TextView textView_hint;
    private Button button_register;
    private TextView textView_toLogin;

    public RegisterFragment(LoginActivity master)
    {
        this.master = master;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        editText_username = view.findViewById(R.id.editText_username);
        editText_pw = view.findViewById(R.id.editText_pw);
        editText_pw2 = view.findViewById(R.id.editText_pw2);
        textView_hint = view.findViewById(R.id.textView_hint);
        button_register = view.findViewById(R.id.button_login_or_register);
        textView_toLogin = view.findViewById(R.id.textView_toLogin);

        button_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(editText_username.getText() == null || editText_pw.getText() == null || editText_pw2.getText() == null)
                {
                    textView_hint.setText("信息不全");
                    return;
                }

                if(editText_username.getText().toString().length() == 0|| editText_pw.getText().toString().length() == 0 || editText_pw2.getText().toString().length() == 0)
                {
                    textView_hint.setText("信息不全");
                    return;
                }

                if(editText_pw.getText().toString().compareTo(editText_pw2.getText().toString()) != 0)
                {
                    textView_hint.setText("两次密码输入不一致");
                    return;
                }

                final String username = editText_username.getText().toString();
                final String password = editText_pw.getText().toString();

                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Message message = new Message();
                        message.what = 3;
                        Memory.currentActivity.receiveMessage(message);

                        RequestBody requestBody = new FormBody.Builder()
                                .add("username", username)
                                .add(password, password)
                                .build();

                        Request request = new Request.Builder()
                                .url("http://" + Memory.serverIP + ":" + Memory.serverApiPort + "/register")
                                .post(requestBody)
                                .build();

                        OkHttpClient client = new OkHttpClient();
                        try
                        {
                            Response response = client.newCall(request).execute();
                            message = new Message();
                            message.what = 4;
                            message.arg1 = Integer.parseInt(response.body().string());
                            Memory.currentActivity.receiveMessage(message);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        textView_toLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                master.toLogin();
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
