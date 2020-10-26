package edu.bistu.gwclient.automata.status;

import android.os.Message;

import edu.bistu.gwclient.Memory;
import edu.bistu.gwclient.automata.event.Event;
import edu.bistu.gwclient.model.ClientMessage;

public class Status5 extends AbstractStatus
{

    protected Status5(Integer statusNumber)
    {
        super(statusNumber);
        supportedNextStatus.put(5, 4);
        supportedNextStatus.put(6, 8);
    }

    @Override
    public void initialize(Event triggeredEvent)
    {
        Message message = new Message();
        message.what = 2;
        Memory.currentActivity.receiveMessage(message);

        Memory.networkService.sendMessage(ClientMessage.quickJoinRequest());

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(Memory.progressDialogTimeout);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                Memory.automata.receiveEvent(new Event(5, null, System.currentTimeMillis()));
            }
        }).start();
    }
}
