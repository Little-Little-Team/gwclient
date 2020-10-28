package edu.bistu.gwclient;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.bistu.gwclient.automata.event.Event;
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

    private ProgressDialog progressDialog;

    private boolean isInRoom;

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
                    {
                        Log.e(getClass().getName(), "three components transfer incomplete");
                        return;
                    }
                    if(user.getUsername() != null)
                    {
                        Log.d(getClass().getName(), "textView.setText(): old one: " + textView.getText().toString() + ", new one: " + user.getUsername());
                        textView.setText(user.getUsername());
                    }
                    if(user.getAvatar() != null)
                        imageView.setImageBitmap(
                                BitmapFactory.decodeByteArray(user.getAvatar(), 0, user.getAvatar().length));
                }
                else
                    Log.e(getClass().getName(), "user info transfer failed");
            }
            else if(what == 2)
            {
                /* 快速加入 */
                showProgressDialog("正在寻找房间");
            }
            else if(what == 4)
            {
                /* 创建房间 */
                showProgressDialog("正在创建房间");
            }
            else if(what == 5)
            {
                /* 快速加入/创建房间服务器响应超时 */
                closeProgressDialog();
                Toast.makeText(MainActivity.this, "服务器响应超时", Toast.LENGTH_SHORT).show();
            }
            else if(what == 6)
            {
                /* 加入房间 */
                closeProgressDialog();
                unlockUI();
                toRoom(msg.arg1);
            }
            else if(what == 7)
            {
                /* 更新房间内玩家信息 */
                if(msg.obj instanceof Long[])
                    roomFragment.updateData((Long[]) msg.obj);
                else
                    Log.e(getClass().getName(), "players info array transfer failed");
            }
            else if(what == 8)
            {
                /* 返回大厅 */
                toHall();
            }
            else if(what == 9)
            {
                finish();
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

        toHall();
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

    @Override
    public void onBackPressed()
    {
        if(isInRoom)
        {
            Toast.makeText(this, "请先离开房间", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("确定要登出吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        Memory.automata.receiveEvent(
                                new Event(25, null, System.currentTimeMillis()));
                    }
                })
                .setNegativeButton("取消", null)
                .setTitle("登出")
                .create();

        alertDialog.show();
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

    private void toHall()
    {
        isInRoom = false;
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

    private void toRoom(Integer roomID)
    {
        Log.d(getClass().getName(), "toRoom(): room id is" + roomID);
        isInRoom = true;
        if(roomFragment == null)
            roomFragment = new RoomFragment(this, roomID);
        roomFragment.setRoomID(roomID);
        replaceFragment(roomFragment);
    }

    public void onQuickJoinClicked()
    {
        if(uilock)
        {
            Log.e(getClass().getName(), "onQuickJoinClicked(): ui is locked");
            return;
        }
        lockUI();
        Memory.automata.receiveEvent(new Event(4, null, System.currentTimeMillis()));
    }

    public void onRoomListClicked()
    {

    }

    public void onCreateRoomClicked()
    {
        Memory.automata.receiveEvent(new Event(7, null, System.currentTimeMillis()));
    }

    /**
     * 异步获取指定用户的信息（包括用户名、头像）
     * @param id 用户ID
     */
    public void setUserPropertyInBackground(Long id, TextView textView, ImageView imageView)
    {
        if(id == null)
        {
            Log.e(getClass().getName(), "setUserProperty(): id is null");
            return;
        }

        UserPropertySetter setter = new UserPropertySetter(id, textView, imageView);
        threadPool.execute(setter);
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.recyclerView_chatList, fragment);
        fragmentTransaction.commit();
    }
}
