package edu.bistu.gwclient.automata.status;

import android.os.Message;
import android.util.Log;

import edu.bistu.gwclient.LoginActivity;
import edu.bistu.gwclient.MainActivity;
import edu.bistu.gwclient.Memory;
import edu.bistu.gwclient.automata.event.Event;

public class Status4 extends AbstractStatus
{
    protected Status4(Integer statusNumber)
    {
        super(statusNumber);
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
    }
}
