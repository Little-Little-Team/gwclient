package edu.bistu.gwclient.automata.status;

import edu.bistu.gwclient.automata.event.Event;

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

    }
}
