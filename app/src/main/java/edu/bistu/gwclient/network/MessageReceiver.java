package edu.bistu.gwclient.network;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import edu.bistu.gwclient.Memory;
import edu.bistu.gwclient.automata.event.Event;
import edu.bistu.gwclient.model.ServerMessage;

public class MessageReceiver implements Runnable
{
    private SocketChannel socketChannel;

    private boolean keepRunning;

    MessageReceiver(SocketChannel socketChannel)
    {
        this.socketChannel = socketChannel;
        keepRunning = true;
    }

    @Override
    public void run()
    {
        Log.d(getClass().getName(), "message receiver start");

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

        try
        {
            Selector selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_READ);

            while(keepRunning)
            {
                selector.select(2000);  //block 2s
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext())
                {
                    SelectionKey selectionKey = iterator.next();
                    if(selectionKey.isReadable())
                    {
                        SelectableChannel selectableChannel = selectionKey.channel();
                        if(selectableChannel instanceof SocketChannel)
                        {
                            SocketChannel socketChannel = (SocketChannel) selectableChannel;

                            int length = socketChannel.read(byteBuffer);
                            Log.d(getClass().getName(), "read message length: " + length);

                            if(length > 0)
                                getMessageFromBuffer(byteBuffer);
                        }
                        else
                            Log.e(getClass().getName(), "select illegal channel");
                    }
                    iterator.remove();
                }
            }

            selector.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Log.d(getClass().getName(), "message receiver end");
    }

    private void getMessageFromBuffer(ByteBuffer byteBuffer)
    {
        byteBuffer.limit(byteBuffer.position());
        byteBuffer.position(0);

        while (byteBuffer.position() < byteBuffer.limit())
        {
            ServerMessage message = ServerMessage.loadFromByteBuffer(byteBuffer);
            messageToEvent(message);
        }

        byteBuffer.limit(byteBuffer.capacity());
        byteBuffer.position(0);
    }

    private void messageToEvent(ServerMessage message)
    {
        if(message == null)
        {
            Log.e(getClass().getName(), "messageToEvent(): message is null");
            return;
        }

        int type = message.getMsgType();
        if(type == 1)
            Memory.automata.receiveEvent(
                    new Event(3, message.getArrLong()[0], message.getTime()));
        else if(type == 2)
            Memory.automata.receiveEvent(
                    new Event(4, message.getArrInt()[0], message.getTime()));
        else if(type == 4)
            Memory.automata.receiveEvent(
                    new Event(6, message.getArrLong()[0], message.getTime()));
    }

    public void shutdown()
    {
        keepRunning = false;
    }

}
