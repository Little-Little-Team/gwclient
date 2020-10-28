package edu.bistu.gwclient.network;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;

import edu.bistu.gwclient.Memory;
import edu.bistu.gwclient.automata.event.Event;
import edu.bistu.gwclient.model.ClientMessage;

public class NetworkService implements Runnable
{
    private MessageReceiver messageReceiver;
    private Thread receiverThread;

    private LinkedBlockingQueue<ClientMessage> messageQueue;

    @Override
    public void run()
    {
        Log.d(getClass().getName(), "network service start");

        messageQueue = new LinkedBlockingQueue<>();

        try
        {
            final SocketChannel socketChannel = SocketChannel.open();
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        Thread.sleep(3000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    if(!socketChannel.isConnected())
                    {
                        Memory.automata.receiveEvent(new Event(26, null, System.currentTimeMillis()));
                        try
                        {
                            socketChannel.close();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        Log.e(getClass().getName(), "network service start failed");
                    }
                }
            }).start();
            socketChannel.connect(new InetSocketAddress(Memory.serverIP, Memory.serverSocketPort));

            socketChannel.configureBlocking(false);

            messageReceiver = new MessageReceiver(socketChannel);
            receiverThread = new Thread(messageReceiver, "MessageReceiver");
            receiverThread.start();

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

            Memory.automata.receiveEvent(new Event(2, null, System.currentTimeMillis()));

            while(true)
            {
                ClientMessage message = messageQueue.take();

                if(message.getId() != null && message.getId() == Long.MIN_VALUE)
                    break;

                message.toByteBuffer(byteBuffer);
                int length = socketChannel.write(byteBuffer);
                Log.d(getClass().getName(), "send a message, length: " + length);
            }

            messageReceiver.shutdown();
            receiverThread.join();
            socketChannel.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Log.d(getClass().getName(), "network service end");
    }

    public void sendMessage(ClientMessage message)
    {
        if(message != null)
            messageQueue.add(message);
        else
            Log.e(getClass().getName(), "network service try to send a null message");
    }
}
