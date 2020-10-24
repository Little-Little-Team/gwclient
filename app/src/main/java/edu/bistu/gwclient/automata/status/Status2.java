package edu.bistu.gwclient.automata.status;

import edu.bistu.gwclient.automata.event.Event;

public class Status2 extends AbstractStatus
{
    protected Status2(Integer statusNumber)
    {
        super(statusNumber);
        supportedNextStatus.put(2, 3);
    }

    @Override
    public void initialize(Event triggeredEvent)
    {

    }
}
