package edu.bistu.gwclient.automata.status;

import edu.bistu.gwclient.Memory;
import edu.bistu.gwclient.automata.event.Event;
import edu.bistu.gwclient.network.NetworkService;

public class Status2 extends AbstractStatus
{
    protected Status2(Integer statusNumber)
    {
        super(statusNumber);
        supportedNextStatus.put(2, 3);
        supportedNextStatus.put(26, 1);
    }

    @Override
    public void initialize(Event triggeredEvent)
    {
        Memory.networkService = new NetworkService();
        Memory.networkServiceThread = new Thread(Memory.networkService);
        Memory.networkServiceThread.start();
    }
}
