package edu.bistu.gwclient.automata.status;

import android.os.Message;
import android.telephony.mbms.MbmsErrors;
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
        supportedNextStatus.put(15, 8);
        supportedNextStatus.put(16, 8);
        supportedNextStatus.put(17, 8);
        supportedNextStatus.put(18, 9);
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
        else if(triggeredEvent.getEventNumber() == 16)
        {
            if(triggeredEvent.getAttachment() instanceof Integer)
                Memory.networkService.sendMessage(
                        ClientMessage.switchPrepareStatus((Integer)triggeredEvent.getAttachment()));
            else
                Log.e(getClass().getName(), "switch prepare status: transfer room id failed");
        }
        else if(triggeredEvent.getEventNumber() == 17)
        {
            if(triggeredEvent.getAttachment() instanceof Integer)
                Memory.networkService.sendMessage(
                        ClientMessage.startGameRequest((Integer)triggeredEvent.getAttachment()));
            else
                Log.e(getClass().getName(), "start game request: transfer room id failed");
        }
        else if(triggeredEvent.getEventNumber() == 24)
        {
            Message message = new Message();
            message.what = 4;
            Memory.currentActivity.receiveMessage(message);
        }
    }
}
