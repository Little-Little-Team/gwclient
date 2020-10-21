package edu.bistu.gwclient.automata.event;

public class Event
{
    /**
     * 事件类
     */

    private Integer eventNumber;

    private Object attachment;

    private Long eventTime;

    public Event(Integer eventNumber, Object attachment, Long eventTime)
    {
        this.eventNumber = eventNumber;
        this.attachment = attachment;
        this.eventTime = eventTime;
    }

    public Integer getEventNumber()
    {
        return eventNumber;
    }

    public Object getAttachment()
    {
        return attachment;
    }

    public Long getEventTime()
    {
        return eventTime;
    }
}
