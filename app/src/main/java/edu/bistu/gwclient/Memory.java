package edu.bistu.gwclient;

import edu.bistu.gwclient.automata.Automata;
import edu.bistu.gwclient.network.NetworkService;

public class Memory
{
    public static volatile CustomActivity currentActivity;

    public static volatile Automata automata;
    public static volatile Thread automataThread;

    public static volatile Thread networkServiceThread;
    public static volatile NetworkService networkService;

    public final static String serverIP = "192.168.2.233";

    public final static String serverApiPort = "8080";
    public final static int serverSocketPort = 4966;

    public static volatile Long id = null;

    public final static long progressDialogTimeout = 3000;
}
