package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class Server {
    public static void main(String[] args) {
        try 
        {
            ServerSocket server = new ServerSocket(8080);
            System.out.println("Start.");
            Socket socket = server.accept();
            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            System.out.println(bufferedReader.readLine());
            System.out.println("Done.");
        }
        catch(IOException e)
        {
            System.err.println("Failed to start server");
            e.printStackTrace();
        }
    }
}
