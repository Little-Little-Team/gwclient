package edu.bistu.gwclient.network;

import android.util.Log;

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

        try
        {
            SocketChannel socketChannel = SocketChannel.open();
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

                if(message.getId() == -1)
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        Log.d(getClass().getName(), "network service end");
    }
}
