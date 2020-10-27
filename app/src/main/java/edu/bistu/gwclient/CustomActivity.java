package edu.bistu.gwclient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class CustomActivity extends AppCompatActivity
{
    /**
     * 自定义活动抽象类
     * 在此类中规定了GuessWhat安卓客户端每一个具体活动类的通用结构和功能
     */

    protected class Handler extends android.os.Handler
    {
        @Override
        public void handleMessage(@NonNull Message msg)
        {
            int what = msg.what;
            if(what == 0)
            {
                /* 切换Activity */
                Intent intent = null;
                int arg = msg.arg1;

                if(arg == 1)
                {
                    /* LoginActivity */
                    intent = new Intent(CustomActivity.this, LoginActivity.class);
                }
                else if(arg == 2)
                {
                    /* MainActivity */
                    intent = new Intent(CustomActivity.this, MainActivity.class);
                }
                else if(arg == 3)
                {
                    /* GameActivity */
                    intent = new Intent(CustomActivity.this, GameActivity.class);
                    intent.putExtra("gameID", msg.arg2);
                }
                else if(arg == 4)
                {

                }

                startActivity(intent);
                if(CustomActivity.this instanceof InitialActivity)
                    finish();
            }
        }
    }

    protected Handler handler;

    protected void initialize()
    {
        Log.d(getClass().getName(), "initialize()");
        setUI();
        setHandler();
        Memory.currentActivity = this;
    }

    protected abstract void setUI();

    protected abstract void setHandler();

    public void receiveMessage(Message message)
    {
        handler.sendMessage(message);
    }

    protected abstract void lockUI();

    protected abstract void unlockUI();

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initialize();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Memory.currentActivity = this;
    }
}
