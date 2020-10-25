package edu.bistu.gwclient.model;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * message from server
 */
public class ServerMessage
{
    // 消息发送时间
    private Long time;

    // 由客户端据此决定拿到消息之后判别后续处理方式
    private Integer msgType;

    // 后续消息数组长度
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

    public static ServerMessage loadFromByteBuffer(ByteBuffer buffer) throws BufferUnderflowException
    {
        ServerMessage message = new ServerMessage();
        message.time = buffer.getLong();
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
                        int strLen = buffer.getInt();
                        StringBuilder builder = new StringBuilder();
                        for (int j = 0; j < strLen; ++j)
                            builder.append(buffer.getChar());
                        message.arrString[i] = builder.toString();
                    }
                    break;
            }
        }

        return message;
    }

    public ByteBuffer toByteBuffer()
    {
        ByteBuffer buffer = ByteBuffer.allocateDirect(this.byteSize());

        buffer.putLong(this.time);
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

        buffer.position(0);

        return buffer;
    }

    // 计算一条消息在发送时需要占用的字节
    private int byteSize()
    {
        int result = 8 + 4 + 4;
        if (this.n > 0)
        {
            result += 4;
            switch (dataType)
            {
                case 1:
                    result += 4 * this.arrInt.length;
                    break;
                case 2:
                    result += 8 * this.arrLong.length;
                    break;
                case 3:
                    result += 4 * this.arrString.length;
                    for (String s : this.arrString)
                        result += s.length() * 2;
                    break;
            }
        }
        return result;
    }

    public Long getTime()
    {
        return time;
    }

    public void setTime(Long time)
    {
        this.time = time;
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

    public Integer getDataType()
    {
        return dataType;
    }

    public Integer[] getArrInt()
    {
        return arrInt;
    }

    public void setArrInt(Integer[] arrInt)
    {
        this.arrInt = arrInt;
        this.n = arrInt.length;
        this.dataType = 1;
    }

    public Long[] getArrLong()
    {
        return arrLong;
    }

    public void setArrLong(Long[] arrLong)
    {
        this.arrLong = arrLong;
        this.n = arrLong.length;
        this.dataType = 2;
    }

    public String[] getArrString()
    {
        return arrString;
    }

    public void setArrString(String[] arrString)
    {
        this.arrString = arrString;
        this.n = arrString.length;
        this.dataType = 3;
    }
}
