package edu.bistu.gwclient.model;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import edu.bistu.gwclient.Memory;

/**
 * message from client
 */
public class ClientMessage
{
    // 用户的唯一id
    private Long id;

    // 消息发送时间
    private Long time;

    // 分发时据此标识, 将消息分发给特定的Service
    // 0 -> NetworkService
    // 1 -> UserService
    // 2 -> RoomService
    // 3 -> GameService
    private Integer serviceNumber;

    // 由特定的接收Service确定如何处理后续消息内容
    private Integer msgType;

    // 后续消息的长度
    private Integer n;

    // 后续消息的格式, 三个数组最终只有一个真正保存数据
    // 1 -> Integer[]
    // 2 -> Long[]
    // 3 -> String[]
    // String[] -> int, char, char, int, char, char
    private Integer dataType;

    private Integer[] arrInt;
    private Long[] arrLong;
    private String[] arrString;

    /**
     * 从ByteBuffer中构造Message对象, 失败则会抛出异常
     * @param buffer 构造对象到数据来源
     * @return 成功构造到Message对象
     * @throws BufferUnderflowException
     */
    public static ClientMessage loadFromByteBuffer(ByteBuffer buffer) throws BufferUnderflowException
    {
        ClientMessage message = new ClientMessage();

        message.id = buffer.getLong();
        message.time = buffer.getLong();
        message.serviceNumber = buffer.getInt();
        message.msgType = buffer.getInt();

        message.n = buffer.getInt();
        if (message.n > 0)
        {
            message.dataType = buffer.getInt();

            switch (message.dataType)
            {
                case 1:
                    message.arrInt = new Integer[message.n];
                    for (int i = 0; i < message.arrInt.length; ++i)
                        message.arrInt[i] = buffer.getInt();
                    break;
                case 2:
                    message.arrLong = new Long[message.n];
                    for (int i = 0; i < message.arrLong.length; ++i)
                        message.arrLong[i] = buffer.getLong();
                    break;
                case 3:
                    message.arrString = new String[message.n];
                    for (int i = 0; i < message.arrString.length; ++i)
                    {
                        int len = buffer.getInt();
                        StringBuilder strBuilder = new StringBuilder(len);
                        for (int j = 0; j < len; ++j)
                            strBuilder.append(buffer.getChar());
                        message.arrString[i] = strBuilder.toString();
                    }
                    break;
            }
        }

        return message;
    }

    public void toByteBuffer(ByteBuffer buffer)
    {
        buffer.limit(buffer.capacity());
        buffer.position(0);

        // 向buffer中填充内容
        buffer.putLong(this.id);
        buffer.putLong(this.time);
        buffer.putInt(this.serviceNumber);
        buffer.putInt(this.msgType);
        buffer.putInt(this.n);

        if (this.n > 0)
        {
            buffer.putInt(this.dataType);

            switch (this.dataType)
            {
                case 1:
                    for (Integer x : this.arrInt)
                        buffer.putInt(x);
                    break;
                case 2:
                    for (Long x : this.arrLong)
                        buffer.putLong(x);
                    break;
                case 3:
                    for (String s : this.arrString)
                    {
                        buffer.putInt(s.length());
                        for (int i = 0; i < s.length(); ++i)
                            buffer.putChar(s.charAt(i));
                    }
                    break;
            }
        }

        buffer.limit(buffer.position());
        buffer.position(0);
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getTime()
    {
        return time;
    }

    public void setTime(Long time)
    {
        this.time = time;
    }

    public Integer getServiceNumber()
    {
        return serviceNumber;
    }

    public void setServiceNumber(Integer serviceNumber)
    {
        this.serviceNumber = serviceNumber;
    }

    public Integer getMsgType()
    {
        return msgType;
    }

    public void setMsgType(Integer msgType)
    {
        this.msgType = msgType;
    }

    public Integer getN()
    {
        return n;
    }

    public void setN(Integer n)
    {
        this.n = n;
    }

    public Integer getDataType()
    {
        return dataType;
    }

    public void setDataType(Integer dataType)
    {
        this.dataType = dataType;
    }

    public Integer[] getArrInt()
    {
        return arrInt;
    }

    public void setArrInt(Integer[] arrInt)
    {
        this.arrInt = arrInt;
    }

    public Long[] getArrLong()
    {
        return arrLong;
    }

    public void setArrLong(Long[] arrLong)
    {
        this.arrLong = arrLong;
    }

    public String[] getArrString()
    {
        return arrString;
    }

    public void setArrString(String[] arrString)
    {
        this.arrString = arrString;
    }

    public static ClientMessage loginRequest(String username, String pw)
    {
        ClientMessage message = new ClientMessage();
        message.setId(0L);
        message.setTime(System.currentTimeMillis());
        message.setServiceNumber(1);
        message.setMsgType(1);
        message.setN(2);
        message.setDataType(3);
        message.setArrString(new String[]{username, pw});
        return message;
    }

    public static ClientMessage quickJoinRequest()
    {
        ClientMessage message = new ClientMessage();
        message.setId(Memory.id);
        message.setTime(System.currentTimeMillis());
        message.setServiceNumber(2);
        message.setMsgType(2);
        message.setN(0);
        return message;
    }

    public static ClientMessage createRoomRequest()
    {
        ClientMessage message = new ClientMessage();
        message.setId(Memory.id);
        message.setTime(System.currentTimeMillis());
        message.setServiceNumber(2);
        message.setMsgType(4);
        message.setN(0);
        return message;
    }

    public static ClientMessage networkServiceShutdownSignal()
    {
        ClientMessage message = new ClientMessage();
        message.setId(Long.MIN_VALUE);
        return message;
    }
}
