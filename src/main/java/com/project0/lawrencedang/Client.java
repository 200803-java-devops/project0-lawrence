package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.PrintStream;

public class Client {

    private BufferedReader reader;
    private PrintStream writer;
    private GameState state;
    public Client(BufferedReader br, PrintStream ps)
    {
        reader = br;
        writer = ps;
    }

    public void run()
    {
        waitForIntro();
        while(true)
        {

        }
    }
    
}