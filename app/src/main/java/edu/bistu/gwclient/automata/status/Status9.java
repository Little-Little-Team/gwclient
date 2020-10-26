package edu.bistu.gwclient.automata.status;

import android.os.Message;
import android.util.Log;

import edu.bistu.gwclient.Memory;
import edu.bistu.gwclient.automata.event.Event;

public class Status9 extends AbstractStatus
{

    protected Status9(Integer statusNumber)
    {
        super(statusNumber);
        supportedNextStatus.put(19, 10);
    }

    @Override
    public void initialize(Event triggeredEvent)
    {
        if(triggeredEvent.getEventNumber() == 18)
        {
            if(triggeredEvent.getAttachment() instanceof Integer)
            {
                Message message = new Message();
                message.what = 0;
                message.arg1 = 3;
                message.arg2 = (Integer) triggeredEvent.getAttachment();
                Memory.currentActivity.receiveMessage(message);
            }
            else
                Log.e(getClass().getName(), "transfer game id failed");
        }
    }
}
