package edu.bistu.gwclient.automata.status;

import android.os.Message;
import android.util.Log;

import edu.bistu.gwclient.LoginActivity;
import edu.bistu.gwclient.Memory;
import edu.bistu.gwclient.automata.event.Event;
import edu.bistu.gwclient.model.ClientMessage;

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

        if(triggeredEvent != null && triggeredEvent.getEventNumber() == 4)
        {
            Message message = new Message();
            message.what = 2;
            Object attachment = triggeredEvent.getAttachment();
            if(attachment instanceof Integer)
                message.arg1 = (Integer) attachment;
            else
                Log.e(getClass().getName(), "cannot transfer error code from attachment");

            /* 关闭网络服务 */
            Memory.networkService.sendMessage(ClientMessage.networkServiceShutdownSignal());
            try
            {
                Memory.networkServiceThread.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            Memory.currentActivity.receiveMessage(message);
        }
    }
}
