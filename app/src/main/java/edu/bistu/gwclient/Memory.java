package edu.bistu.gwclient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.bistu.gwclient.automata.Automata;
import edu.bistu.gwclient.model.User;
import edu.bistu.gwclient.network.NetworkService;

public class Memory
{
    public static volatile CustomActivity currentActivity;

    public static volatile Automata automata;
    public static volatile Thread automataThread;

    public static volatile Thread networkServiceThread;
    public static volatile NetworkService networkService;

    public final static String serverIP = "10.16.55.114";

    public final static String serverApiPort = "28080";
    public final static int serverSocketPort = 24966;

    public static volatile Long id = null;

    public final static long progressDialogTimeout = 3000;

    public final static int maxRoomSize = 8;

    public final static Map<Long, User> userCache = new ConcurrentHashMap<>();
}
