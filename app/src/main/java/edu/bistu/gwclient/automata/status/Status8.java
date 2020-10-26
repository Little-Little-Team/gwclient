package edu.bistu.gwclient.automata.status;

import android.os.Message;

import edu.bistu.gwclient.Memory;
import edu.bistu.gwclient.automata.event.Event;

public class Status8 extends AbstractStatus
{

    protected Status8(Integer statusNumber)
    {
        super(statusNumber);
        supportedNextStatus.put(14, 4);
    }

    @Override
    public void initialize(Event triggeredEvent)
    {
        if(triggeredEvent.getEventNumber() == 6)
        {
            Message message = new Message();
            message.what = 6;
            message.obj = triggeredEvent.getAttachment();
            Memory.currentActivity.receiveMessage(message);
        }
    }
}
