package edu.bistu.gwclient.automata.status;

import android.os.Message;

import edu.bistu.gwclient.Memory;
import edu.bistu.gwclient.automata.event.Event;
import edu.bistu.gwclient.model.ClientMessage;

public class Status7 extends AbstractStatus
{

    protected Status7(Integer statusNumber)
    {
        super(statusNumber);
        supportedNextStatus.put(6, 8);
        supportedNextStatus.put(8, 4);
    }

    @Override
    public void initialize(Event triggeredEvent)
    {
        Message message = new Message();
        message.what = 2;
        Memory.currentActivity.receiveMessage(message);

        Memory.networkService.sendMessage(ClientMessage.createRoomRequest());

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
                Memory.automata.receiveEvent(new Event(8, null, System.currentTimeMillis()));
            }
        }).start();
    }
}
