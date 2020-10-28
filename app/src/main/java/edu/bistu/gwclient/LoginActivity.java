package edu.bistu.gwclient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

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
                loginFragment.setHintContent("登录失败，错误代码：" + msg.arg1);
            }
            else if(what == 3)
            {
                showProgressDialog("正在注册");
            }
            else if(what == 4)
            {
                closeProgressDialog();
                String str;
                if(msg.arg1 == 1)
                {
                    toLogin();
                    str = "注册成功";
                    Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(msg.arg1 == 2)
                    str = "用户已存在";
                else if(msg.arg1 == 3)
                    str = "密码过于简单（长度小于4位）";
                else
                    str = "非法参数";
                registerFragment.setHintContent(str);
            }
            else if(what == 5)
            {
                closeProgressDialog();
                loginFragment.setHintContent("与服务器连接失败");
            }
        }
    }


    @Override
    protected void setUI()
    {
        setContentView(R.layout.activity_login);
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
    protected void onDestroy()
    {
        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("确定要退出游戏吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        Memory.automata.receiveEvent(
                                new Event(Integer.MIN_VALUE, null, System.currentTimeMillis()));
                        finish();
                    }
                })
                .setNegativeButton("取消", null)
                .setTitle("退出程序？")
                .create();

        alertDialog.show();
    }

    @Override
    protected void onStop()
    {
        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        super.onStop();
    }

    public void onLoginButtonClicked(String username, String pw)
    {
        if(uilock)
        {
            Log.e(getClass().getName(), "loginButtonClicked but ui is locked");
            return;
        }

        lockUI();
        if(username == null || username.length() == 0 || pw == null || pw.length() == 0)
        {
            loginFragment.setHintContent("用户名或密码为空");
            unlockUI();
            return;
        }

        Log.d(getClass().getName(), "loginButtonClicked(" + username + ", " + pw + ")");
        showProgressDialog("正在连接服务器");
        unlockUI();
        inputUserName = username;
        inputPw = pw;
        Memory.automata.receiveEvent(new Event(1, null, System.currentTimeMillis()));
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
        fragmentTransaction.replace(R.id.recyclerView_chatList, fragment);
        fragmentTransaction.commit();
    }
}
