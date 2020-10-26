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
        statusMap.put(2, new Status2(2));
        statusMap.put(3, new Status3(3));
        statusMap.put(4, new Status4(4));
        statusMap.put(5, new Status5(5));
        statusMap.put(7, new Status7(7));
        statusMap.put(8, new Status8(8));
        statusMap.put(9, new Status9(9));
        statusMap.put(10, new Status10(10));
        statusMap.put(11, new Status11(11));
    }

    public AbstractStatus getStatus(Integer statusNumber)
    {
        return statusMap.get(statusNumber);
    }
}
