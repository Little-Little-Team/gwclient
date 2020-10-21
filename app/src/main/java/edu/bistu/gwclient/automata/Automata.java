package edu.bistu.gwclient.automata;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

import edu.bistu.gwclient.automata.event.Event;
import edu.bistu.gwclient.automata.status.AbstractStatus;
import edu.bistu.gwclient.automata.status.StatusFactory;

public class Automata implements Runnable
{
    /**
     * 控制界面与后台交互逻辑的核心
     * 以有向图结构表示并存储自动机的执行逻辑，图结点表示“状态”，结点间的连线表示“事件”
     * 用户对客户端的互动行为（主要体现为点击手机屏幕），以及接收来自服务器的消息，均可触发某种“事件”
     * 每一个“状态”均可由指定的某些“事件”，转换成另一个“状态”
     * 在自动机中维护一个变量“currentStatus”，表示自动机的“当前状态”
     * 自动机通过刚刚被触发的“事件”，从有向图中查找下一“状态”，并更新为“当前状态”
     * 自动机独立运行在一个线程中，通过Message向界面线程发送指令以控制界面的变化
     * 界面线程以及其他线程通过向自动机的“事件队列”发送【刚刚被触发的事件】
     * 自动机的任务就是根据被触发的“事件”，转换“当前状态”，并调用新“状态”的某个方法，以执行一系列逻辑
     *
     * 自动机存储并调用的所有“状态”对象的类型，均为“状态”的抽象父类
     * 不同的“状态”通过继承同一个抽象父类，在同一个抽象方法中写入不同的具体实现，供自动机线程调用
     */

    private LinkedBlockingQueue<Event> eventQueue;  //事件队列

    private StatusFactory statusFactory;    //状态工厂

    private AbstractStatus currentStatus;   //当前状态

    @Override
    public void run()
    {
        /* 自动机线程所执行的逻辑于此处开始 */

        Log.d(getClass().getName(), "自动机启动");

        /* 初始化数据 */
        eventQueue = new LinkedBlockingQueue<>();
        statusFactory = new StatusFactory();
        currentStatus = statusFactory.getStatus(1); //由第1号状态开始

        try
        {
            Thread.sleep(1500); //线程休眠1.5秒
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        currentStatus.initialize(null); //执行第1号状态的逻辑

        while(true)
        {
            try
            {
                Event event = eventQueue.take();
                Integer eventNumber = event.getEventNumber();

                if(eventNumber == Integer.MIN_VALUE) //规定事件序号为整数最小值时为自动机线程的终止信号
                    break;

                Integer nextStatusNumber = currentStatus.getNextStatus(eventNumber);
                if(nextStatusNumber == null)
                    Log.e(getClass().getName(), "当前状态：" + currentStatus.getStatusNumber() + "，收到不支持的事件：" + eventNumber);
                else
                {
                    /* 状态转换 */
                    Log.d(getClass().getName(), "当前状态：" + currentStatus.getStatusNumber() + "，事件：" + eventNumber + "，下一个状态：" + nextStatusNumber);
                    currentStatus = statusFactory.getStatus(nextStatusNumber);
                    currentStatus.initialize(event);
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }


        Log.d(getClass().getName(), "自动机结束");
    }
}
