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

    public final static String serverIP = "10.16.55.116";

    public final static String serverApiPort = "8080";
    public final static int serverSocketPort = 4966;
}
