package edu.bistu.gwclient.automata.status;

import android.os.Message;
import android.util.Log;

import edu.bistu.gwclient.Memory;
import edu.bistu.gwclient.automata.event.Event;
import edu.bistu.gwclient.model.ClientMessage;

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
            if(triggeredEvent.getAttachment() instanceof Integer)
                message.arg1 = (Integer) triggeredEvent.getAttachment();
            else
                Log.e(getClass().getName(), "room id transfer failed");
            Memory.currentActivity.receiveMessage(message);

            Memory.networkService.sendMessage(ClientMessage.roomInfoRequest(message.arg1));
        }
        else if(triggeredEvent.getEventNumber() == 15)
        {
            Message message = new Message();
            message.what = 7;
            message.obj = triggeredEvent.getAttachment();
            Memory.currentActivity.receiveMessage(message);
        }
    }
}
