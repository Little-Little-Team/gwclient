package edu.bistu.gwclient.automata.status;

import java.util.HashMap;
import java.util.Map;

public class StatusFactory
{
    /**
     * 创建并储存状态对象的工厂类
     */

    private Map<Integer, AbstractStatus> statusMap;

    public StatusFactory()
    {
        statusMap = new HashMap<>();
        initialize();
    }

    private void initialize()
    {
        statusMap.put(1, new Status1(1));
    }

    public AbstractStatus getStatus(Integer statusNumber)
    {
        return statusMap.get(statusNumber);
    }
}
