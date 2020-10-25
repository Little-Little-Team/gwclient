package edu.bistu.gwclient.task;

import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import edu.bistu.gwclient.Memory;
import edu.bistu.gwclient.model.User;

public class UserPropertySetter implements Runnable
{
    private Long id;

    private TextView textView_username;
    private ImageView imageView_avatar;

    public UserPropertySetter(Long id, TextView textView_username, ImageView imageView_avatar)
    {
        this.id = id;
        this.textView_username = textView_username;
        this.imageView_avatar = imageView_avatar;
    }

    @Override
    public void run()
    {
        Log.d(getClass().getName(), "downloading user property, id: " + id);

        User user = new User();

        Message message = new Message();
        message.what = 1;
        message.obj = new Object[]{textView_username, imageView_avatar, user};
        Memory.currentActivity.receiveMessage(message);
    }
}
