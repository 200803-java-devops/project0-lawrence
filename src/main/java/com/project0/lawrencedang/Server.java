package com.project0.lawrencedang;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class Server {

    public static void main(String[] args) {
        ServerSocket server = null;
        try 
        {
            server = new ServerSocket(8080);
        }
        catch(IOException e)
        {
            System.err.println("Failed to start server");
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Start.");
        Socket socket = null;
        while(true)
        {
            try
            {
                socket = server.accept();
            }
            catch (IOException e)
            {
                System.err.println("Problem while listening for connections.");
            }
            ConnectionHandler handler = new ConnectionHandler(socket);
            new Thread(handler).start();
        }
            
        
    }
}
