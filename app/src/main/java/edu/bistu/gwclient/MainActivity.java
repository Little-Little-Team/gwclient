package edu.bistu.gwclient;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.bistu.gwclient.fragment.HallFragment;
import edu.bistu.gwclient.fragment.RoomFragment;
import edu.bistu.gwclient.fragment.RoomListFragment;
import edu.bistu.gwclient.model.User;
import edu.bistu.gwclient.task.UserPropertySetter;

public class MainActivity extends CustomActivity
{
    private ExecutorService threadPool;

    private TextView textView_username;
    private ImageView imageView_avatar;
    private ImageView imageView_setting;

    private HallFragment hallFragment;
    private RoomListFragment roomListFragment;
    private RoomFragment roomFragment;

    private boolean uilock;

    @SuppressLint("HandlerLeak")
    private class Handler extends CustomActivity.Handler
    {
        @Override
        public void handleMessage(@NonNull Message msg)
        {
            super.handleMessage(msg);
            int what = msg.what;
            if(what == 1)
            {
                /* 显示用户信息 */
                Object obj = msg.obj;
                if(obj instanceof Object[])
                {
                    Object[] arr = (Object[]) obj;
                    if(arr.length != 3)
                    {
                        Log.e(getClass().getName(), "user info data array has illegal length: " + arr.length);
                        return;
                    }
                    TextView textView = null;
                    ImageView imageView = null;
                    User user = null;
                    if(arr[0] instanceof TextView)
                        textView = (TextView) arr[0];
                    if(arr[1] instanceof ImageView)
                        imageView = (ImageView) arr[1];
                    if(arr[2] instanceof User)
                        user = (User) arr[2];
                    if(textView == null || imageView == null || user == null)
                        Log.e(getClass().getName(), "three components transfer incomplete");
                    if(user.getUsername() != null)
                        textView.setText(user.getUsername());
                    if(user.getAvatar() != null)
                        imageView.setImageBitmap(
                                BitmapFactory.decodeByteArray(user.getAvatar(), 0, user.getAvatar().length));
                }
                else
                    Log.e(getClass().getName(), "user info transfer failed");
            }
        }
    }

    @Override
    protected void setUI()
    {
        threadPool = Executors.newCachedThreadPool();   //开启线程池

        setContentView(R.layout.activity_main);
        textView_username = findViewById(R.id.textView_name);
        imageView_avatar = findViewById(R.id.imageView_avatar);
        imageView_setting = findViewById(R.id.imageView_setting);

        imageView_setting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });
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
    protected void onResume()
    {
        super.onResume();
        setUserPropertyInBackground(Memory.id, textView_username, imageView_avatar);
    }

    @Override
    protected void onDestroy()
    {
        threadPool.shutdownNow();
        super.onDestroy();
    }

    private void toHall()
    {
        if(hallFragment == null)
            hallFragment = new HallFragment(this);
        replaceFragment(hallFragment);
    }

    private void toRoomList()
    {
        if(roomListFragment == null)
            roomListFragment = new RoomListFragment(this);
        replaceFragment(roomListFragment);
    }

    private void toRoom()
    {
        if(roomFragment == null)
            roomFragment = new RoomFragment(this);
        replaceFragment(roomFragment);
    }

    /**
     * 异步获取指定用户的信息（包括用户名、头像）
     * @param id 用户ID
     */
    private void setUserPropertyInBackground(Long id, TextView textView, ImageView imageView)
    {
        if(id == null)
        {
            Log.e(getClass().getName(), "setUserProperty(): id is null");
            return;
        }

        UserPropertySetter setter = new UserPropertySetter(id, textView_username, imageView_avatar);
        threadPool.execute(setter);
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
