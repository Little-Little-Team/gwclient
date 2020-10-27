package edu.bistu.gwclient.task;

import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import edu.bistu.gwclient.Memory;
import edu.bistu.gwclient.model.User;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        User user = Memory.userCache.get(id);
        if(user != null)
        {
            function(user);
            return;
        }

        Log.d(getClass().getName(), "downloading user property, id: " + id);

        RequestBody requestBody = new FormBody.Builder()
                .add("id", id.toString())
                .build();

        Request request = new Request.Builder()
                .url("http://" + Memory.serverIP + ":" + Memory.serverApiPort + "/get_username_by_id")
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        String username = null;

        try
        {
            Response response = client.newCall(request).execute();
            username = response.body().string();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        user = new User();
        user.setUsername(username);

        if(username != null)
            Memory.userCache.put(id, user);

        function(user);
    }

    private void function(User user)
    {
        Message message = new Message();
        message.what = 1;
        message.obj = new Object[]{textView_username, imageView_avatar, user};
        Memory.currentActivity.receiveMessage(message);
    }
}
