package edu.bistu.gwclient.automata.status;

import android.os.Message;

import edu.bistu.gwclient.LoginActivity;
import edu.bistu.gwclient.Memory;
import edu.bistu.gwclient.automata.event.Event;

public class Status1 extends AbstractStatus
{

    protected Status1(Integer statusNumber)
    {
        super(statusNumber);
        supportedNextStatus.put(1, 2);
    }

    @Override
    public void initialize(Event triggeredEvent)
    {
        if(!(Memory.currentActivity instanceof LoginActivity))
        {
            Message message = new Message();
            message.what = 0;
            message.arg1 = 1;
            Memory.currentActivity.receiveMessage(message);
        }
    }
}
