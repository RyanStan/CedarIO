package com.ryanstan.web_server_example;

import com.ryanstan.cedario.EventHandler;
import com.ryanstan.cedario.EventType;
import com.ryanstan.cedario.InitiationDispatcher;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;

/*
 * This Handler is instantiated by the RestServiceAcceptor class.  This class handles
 * incoming HTTP requests from a given client connection.
 */
public class RestServiceHandler implements EventHandler {

    private SocketChannel client;

    public RestServiceHandler(SocketChannel client) throws IOException {
        this.client = client;
        InitiationDispatcher dispatcher = InitiationDispatcher.getInstance();
        dispatcher.registerHandler(this, EventType.READ_EVENT);
    }

    public void handleEvent(EventType eventType) throws IOException {
        if (eventType != EventType.READ_EVENT) {
            throw new IllegalArgumentException("RestServiceHandler was called by dispatcher to " +
                    "handle an event that wasn't READ");
        }
        System.out.println("Read initiated...");

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        client.read(buffer);
        String data = new String(buffer.array()).trim();
        if (data.length() > 0) {
            System.out.println("Received message: " + data);
        }
        return;
    }

    // Hook method that returns the underlying NIO Selectable Channel
    public SelectableChannel getSelectableChannel() {
        return client;
    }
}
