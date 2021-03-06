package edu.bistu.gwclient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import edu.bistu.gwclient.adapter.ChatAdapter;
import edu.bistu.gwclient.automata.event.Event;
import edu.bistu.gwclient.fragment.GameResultDialogFragment;
import edu.bistu.gwclient.model.User;

public class GameActivity extends CustomActivity
{
    private TextView textView_title;

    private ProgressBar progressBar;

    private RecyclerView recyclerView_chatList;

    private EditText editText_input;

    private Button button_send;

    private ChatAdapter adapter;

    private MediaPlayer mediaPlayer;

    private MusicDurationUpdater updater;

    private GameResultDialogFragment dialogFragment;

    private boolean isReady;

    private Integer gameID;

    private boolean isDialogShowing;

    private boolean uilock;

    private class Handler extends CustomActivity.Handler
    {
        @Override
        public void handleMessage(@NonNull Message msg)
        {
            super.handleMessage(msg);
            int what = msg.what;
            if(what == 1)
            {
                /* 开始播放音乐 */
                if(msg.obj instanceof Long[])
                    playMusic((Long[]) msg.obj);
                else
                    Log.e(getClass().getName(), "play music: long[] transfer failed");
            }
            else if(what == 2)
            {
                /* 刷新聊天 */
                String[] arr = (String[]) msg.obj;
                Long userID = Long.parseLong(arr[0]);
                User user = Memory.userCache.get(userID);
                String str;
                if(user != null)
                    str = user.getUsername() + ": " + arr[1];
                else
                    str = "uid" + arr[0] + ": " + arr[1];
                updateChat(str);
            }
            else if(what == 3)
            {
                /* 显示结果 */
                updater.shutdown();
                mediaPlayer.reset();
                if(msg.obj instanceof Long[])
                {
                    dialogFragment = new GameResultDialogFragment(GameActivity.this, (Long[]) msg.obj);
                    dialogFragment.show(getSupportFragmentManager(), "GameResult");
                    isDialogShowing = true;
                }
                else
                    Log.e(getClass().getName(), "game result transfer failed");
            }
            else if(what == 4)
            {
                mediaPlayer.release();
                if(dialogFragment != null && isDialogShowing)
                    dialogFragment.dismiss();

                finish();
            }

        }
    }

    private class MusicDurationUpdater implements Runnable
    {
        private boolean keepRunning;

        MusicDurationUpdater()
        {
            keepRunning = true;
        }

        @Override
        public void run()
        {
            Log.d(getClass().getName(), "updater start");
            while(keepRunning)
            {
                if(mediaPlayer.isPlaying())
                    progressBar.setProgress(mediaPlayer.getCurrentPosition());
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            Log.d(getClass().getName(), "updater end");
        }

        void shutdown()
        {
            keepRunning = false;
        }
    }

    @Override
    protected void setUI()
    {
        setContentView(R.layout.activity_game);

        textView_title = findViewById(R.id.textView_title);
        progressBar = findViewById(R.id.progressBar);
        recyclerView_chatList = findViewById(R.id.recyclerView_chatList);
        editText_input = findViewById(R.id.editText_input);
        button_send = findViewById(R.id.button_send);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_chatList.setLayoutManager(manager);
        adapter = new ChatAdapter(this);
        recyclerView_chatList.setAdapter(adapter);

        button_send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Memory.automata.receiveEvent(
                        new Event(20,
                                new Object[]{gameID, editText_input.getText().toString()},
                                System.currentTimeMillis()));
                editText_input.setText("");
            }
        });

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
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        gameID = getIntent().getIntExtra("gameID", 0);
        isReady = false;
    }

    @Override
    protected void onResume()
    {
        if(!isReady)
        {
            Memory.automata.receiveEvent(
                    new Event(19, gameID, System.currentTimeMillis()));

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
            );

            updater = new MusicDurationUpdater();
            new Thread(updater).start();
            isReady = true;
        }

        super.onResume();
    }

    @Override
    public void onBackPressed()
    {
        //do nothing
    }

    private void updateChat(String str)
    {
        adapter.addChat(str);
        adapter.notifyItemInserted(adapter.getItemCount() - 1);
        recyclerView_chatList.scrollToPosition(adapter.getItemCount() - 1);
    }

    @SuppressLint("SetTextI18n")
    private void playMusic(final Long[] arr)
    {
        textView_title.setText("歌曲：" + arr[1] + "/" +arr[2]);
        mediaPlayer.reset();
        try
        {
            mediaPlayer.setDataSource(
                    "http://" + Memory.serverIP + ":" + Memory.serverApiPort + "/get_music?id=" + arr[0]);
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                            .build());
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer)
                {
                    mediaPlayer.start();
                    progressBar.setMax(arr[3].intValue() * 1000);
                    Log.d(getClass().getName(), "music duration: " + mediaPlayer.getDuration());
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener()
            {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1)
                {
                    Toast.makeText(GameActivity.this, "播放错误：" + i, Toast.LENGTH_SHORT).show();
                    playMusic(arr);
                    return true;
                }
            });
            mediaPlayer.prepareAsync();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onConfirmClicked()
    {
        Memory.automata.receiveEvent(new Event(24, null, System.currentTimeMillis()));
    }

}
