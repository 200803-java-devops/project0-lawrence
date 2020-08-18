package com.project0.lawrencedang;

import java.net.ServerSocket;

/**
 * Superclass for servers that listen for connections.
 */
public abstract class Server {
    protected ServerSocket server;

    public Server(ServerSocket server)
    {
        this.server = server;
    }

    public abstract void listen();
}
