package edu.bistu.gwclient.automata.status;

import android.os.Message;

import edu.bistu.gwclient.LoginActivity;
import edu.bistu.gwclient.Memory;
import edu.bistu.gwclient.automata.event.Event;
import edu.bistu.gwclient.model.ClientMessage;

public class Status3 extends AbstractStatus
{
    protected Status3(Integer statusNumber)
    {
        super(statusNumber);
        supportedNextStatus.put(3, 4);
        supportedNextStatus.put(4, 1);
    }

    @Override
    public void initialize(Event triggeredEvent)
    {
        Message message = new Message();
        message.what = 1;
        Memory.currentActivity.receiveMessage(message);

        Memory.networkService.sendMessage(
                ClientMessage.loginRequest(LoginActivity.inputUserName, LoginActivity.inputPw));
    }
}
