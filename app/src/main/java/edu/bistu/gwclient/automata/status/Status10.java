package edu.bistu.gwclient.automata.status;

import android.os.Message;
import android.util.Log;

import edu.bistu.gwclient.Memory;
import edu.bistu.gwclient.automata.event.Event;
import edu.bistu.gwclient.model.ClientMessage;

public class Status10 extends AbstractStatus
{
    protected Status10(Integer statusNumber)
    {
        super(statusNumber);
        supportedNextStatus.put(20, 10);
        supportedNextStatus.put(21, 10);
        supportedNextStatus.put(22, 10);
        supportedNextStatus.put(23, 11);
    }

    @Override
    public void initialize(Event triggeredEvent)
    {
        if(triggeredEvent.getEventNumber() == 19)
        {
            if(triggeredEvent.getAttachment() instanceof Integer)
                Memory.networkService.sendMessage(
                        ClientMessage.gameReady((Integer) triggeredEvent.getAttachment()));
            else
                Log.e(getClass().getName(), "event number 19: gameID transfer failed");
        }
        else if(triggeredEvent.getEventNumber() == 20)
        {
            if(triggeredEvent.getAttachment() instanceof Object[])
            {
                Object[] arr = (Object[]) triggeredEvent.getAttachment();
                if(arr[0] instanceof Integer && arr[1] instanceof String)
                    Memory.networkService.sendMessage(ClientMessage.newChat((Integer) arr[0], (String) arr[1]));
                else
                    Log.e(getClass().getName(), "event number 20: gameID or chat transfer failed");
            }
            else
                Log.e(getClass().getName(), "event number 20: object[] transfer failed");
        }
        else if(triggeredEvent.getEventNumber() == 21)
        {
            if(triggeredEvent.getAttachment() instanceof Long[])
            {
                Message message = new Message();
                message.what = 1;
                message.obj = triggeredEvent.getAttachment();
            }
            else
                Log.e(getClass().getName(), "event number 21: long[] transfer failed");
        }
        else if(triggeredEvent.getEventNumber() == 22)
        {
            if(triggeredEvent.getAttachment() instanceof String[])
            {
                Message message = new Message();
                message.what = 2;
                message.obj = triggeredEvent.getAttachment();
            }
            else
                Log.e(getClass().getName(), "event number 22: string[] transfer failed");
        }
    }
}
