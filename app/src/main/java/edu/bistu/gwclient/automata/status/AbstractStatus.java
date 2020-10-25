package edu.bistu.gwclient.automata.status;

import java.util.HashMap;
import java.util.Map;

import edu.bistu.gwclient.automata.event.Event;

public abstract class AbstractStatus
{
    /**
     * 抽象状态类
     * 每一个具体状态类必须继承此类并实现所有的抽象方法
     */

    protected Integer statusNumber; //状态序号

    protected Map<Integer, Integer> supportedNextStatus;   //可由当前状态转换的下一状态，<事件序号，状态序号>

    protected AbstractStatus(Integer statusNumber)
    {
        this.statusNumber = statusNumber;
        supportedNextStatus = new HashMap<>();
    }

    /**
     * 自动机每转换至一个新状态时调用此方法
     * 所有子类通过重写此方法以实现各自的逻辑
     * @param triggeredEvent 自动机由此事件转换至当前状态
     */
    public abstract void initialize(Event triggeredEvent);

    /**
     * 当某事件触发时，由自动机调用此方法获得新状态的序号
     * @param eventNumber 事件序号
     * @return 新状态序号，若当前状态不支持参数中的事件，则返回null
     */
    public Integer getNextStatus(Integer eventNumber)
    {
        return supportedNextStatus.get(eventNumber);
    }

    public Integer getStatusNumber()
    {
        return statusNumber;
    }
}
