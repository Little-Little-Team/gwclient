package edu.bistu.gwclient;

import edu.bistu.gwclient.automata.Automata;

public class Memory
{
    public static volatile CustomActivity currentActivity;

    public static volatile Automata automata;
    public static volatile Thread automataThread;
}
