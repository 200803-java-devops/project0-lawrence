package com.project0.lawrencedang;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    public static void main(String[] args) {
        try
        {
            Socket socket = new Socket("localhost", 8080);
            OutputStream outputStream = socket.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            outputStreamWriter.write("Hello!\n");
            outputStreamWriter.flush();
            System.out.println("Done.");
        } catch (IOException e) 
        {
            System.err.println("Failed to connect to server.");
        }
    }
}