package edu.bistu.gwclient.automata.status;

import android.os.Message;
import android.util.Log;

import edu.bistu.gwclient.Memory;
import edu.bistu.gwclient.automata.event.Event;

public class Status11 extends AbstractStatus
{

    protected Status11(Integer statusNumber)
    {
        super(statusNumber);
        supportedNextStatus.put(24, 8);
    }

    @Override
    public void initialize(Event triggeredEvent)
    {
        if(triggeredEvent.getAttachment() instanceof Long[])
        {
            Message message = new Message();
            message.what = 3;
            message.obj = triggeredEvent.getAttachment();
            Memory.currentActivity.receiveMessage(message);
        }
        else
            Log.e(getClass().getName(), "game result transfer failed");

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(10000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                Memory.automata.receiveEvent(new Event(24, null, System.currentTimeMillis()));
            }
        }).start();
    }
}
