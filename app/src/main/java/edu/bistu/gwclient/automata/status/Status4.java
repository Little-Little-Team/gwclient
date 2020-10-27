package edu.bistu.gwclient.automata.status;

import android.os.Message;
import android.util.Log;

import edu.bistu.gwclient.LoginActivity;
import edu.bistu.gwclient.MainActivity;
import edu.bistu.gwclient.Memory;
import edu.bistu.gwclient.automata.event.Event;
import edu.bistu.gwclient.model.ClientMessage;

public class Status4 extends AbstractStatus
{
    protected Status4(Integer statusNumber)
    {
        super(statusNumber);
        supportedNextStatus.put(4, 5);
        supportedNextStatus.put(7, 7);
        supportedNextStatus.put(25, 1);
    }

    @Override
    public void initialize(Event triggeredEvent)
    {
        if(!(Memory.currentActivity instanceof MainActivity))
        {
            /* 跳转至MainActivity */
            Message message = new Message();
            message.what = 0;
            message.arg1 = 2;
            Memory.currentActivity.receiveMessage(message);
        }

        if(triggeredEvent.getEventNumber() == 3)
        {
            LoginActivity.inputUserName = null;
            LoginActivity.inputPw = null;

            Object attachment = triggeredEvent.getAttachment();
            if(attachment instanceof Long)
                Memory.id = (Long) attachment;
            else
                Log.e(getClass().getName(), "cannot transfer user id from attachment");
        }
        else if(triggeredEvent.getEventNumber() == 5)
        {

        }
        else if(triggeredEvent.getEventNumber() == 8)
        {

        }
        else if(triggeredEvent.getEventNumber() == 14)
        {
            Message message = new Message();
            message.what = 8;
            Memory.currentActivity.receiveMessage(message);

            if(triggeredEvent.getAttachment() instanceof Integer)
                Memory.networkService.sendMessage(
                        ClientMessage.exitRoom((Integer) triggeredEvent.getAttachment()));
            else
                Log.e(getClass().getName(), "exit room: cannot transfer room id");
        }
    }
}
