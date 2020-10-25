package edu.bistu.gwclient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;

import edu.bistu.gwclient.automata.event.Event;
import edu.bistu.gwclient.fragment.LoginFragment;
import edu.bistu.gwclient.fragment.RegisterFragment;

public class LoginActivity extends CustomActivity
{
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;

    private boolean uilock;

    private ProgressDialog progressDialog;

    public static volatile String inputUserName;
    public static volatile String inputPw;

    private class Handler extends CustomActivity.Handler
    {
        @Override
        public void handleMessage(@NonNull Message msg)
        {
            super.handleMessage(msg);
            int what = msg.what;
            if(what == 1)
            {
                /* 与服务器连接成功，更改对话框提示信息 */
                showProgressDialog("正在登录");
            }
            else if(what == 2)
            {
                /* 登录失败 */
                closeProgressDialog();
                loginFragment.setHintContent("登陆失败，错误代码：" + msg.arg1);
            }
        }
    }


    @Override
    protected void setUI()
    {
        toLogin();
        uilock = false;
    }

    @Override
    protected void setHandler()
    {
        handler = new Handler();
    }

    @Override
    protected void lockUI()
    {
        uilock = true;
    }

    @Override
    protected void unlockUI()
    {
        uilock = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginButtonClicked(String username, String pw)
    {
        if(uilock)
        {
            Log.e(getClass().getName(), "loginButtonClicked but ui is locked");
            return;
        }

        if(username == null || username.length() == 0 || pw == null || pw.length() == 0)
        {
            loginFragment.setHintContent("用户名或密码为空");
            return;
        }

        Log.d(getClass().getName(), "loginButtonClicked(" + username + ", " + pw + ")");
        showProgressDialog("正在连接服务器");
        inputUserName = username;
        inputPw = pw;
        Memory.automata.receiveEvent(new Event(1, null, System.currentTimeMillis()));
        lockUI();
    }

    public void toRegister()
    {
        if(registerFragment == null)
            registerFragment = new RegisterFragment(this);
        replaceFragment(registerFragment);
    }

    public void toLogin()
    {
        if(loginFragment == null)
            loginFragment = new LoginFragment(this);
        replaceFragment(loginFragment);
    }

    private void showProgressDialog(String msg)
    {
        if(progressDialog == null)
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
        }
        progressDialog.setMessage(msg);
        if(!progressDialog.isShowing())
            progressDialog.show();
    }

    private void closeProgressDialog()
    {
        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
