package edu.bistu.gwclient;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import edu.bistu.gwclient.fragment.LoginFragment;
import edu.bistu.gwclient.fragment.RegisterFragment;

public class LoginActivity extends CustomActivity
{
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;

    private boolean uilock;

    @Override
    protected void setUI()
    {
        toLogin();
        uilock = false;
    }

    @Override
    protected void setHandler()
    {

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
            Log.e(getClass().getName(), "ui is locked");
            return;
        }

        if(username == null || username.length() == 0 || pw == null || pw.length() == 0)
        {
            loginFragment.setHintContent("用户名或密码为空");
            return;
        }

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

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
