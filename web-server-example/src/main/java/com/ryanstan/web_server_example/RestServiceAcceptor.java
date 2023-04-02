package com.ryanstan.web_server_example;
import com.ryanstan.cedario.EventHandler;
import com.ryanstan.cedario.EventType;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectableChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * This class listens for incoming connection requests and
 * establishes new connections for the RestServiceHandler
 */
public class RestServiceAcceptor implements EventHandler
{

    private ServerSocketChannel serverSocketChannel;

    /*
     * addr is the address at which the acceptor will open a ServerSocket
     * to listen for incoming connection requests
     */
    public RestServiceAcceptor(InetSocketAddress addr) throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(addr);
    }

    public void handleEvent(EventType eventType) {
        if (eventType != EventType.ACCEPT_EVENT) {
            throw new IllegalArgumentException("RestServiceAcceptor was called by dispatcher to " +
                                                "handle an event that wasn't ACCEPT");
        }
        System.out.println("Connection Accepted...");

        try {
            SocketChannel client = serverSocketChannel.accept();
            client.configureBlocking(false);
            new RestServiceHandler(client);
        } catch (IOException e) {
            System.out.println("LoggingAcceptor: IOError from accept on serverSocketChannel");
        }
    }

    public SelectableChannel getSelectableChannel() {
        return serverSocketChannel;
    }
}
