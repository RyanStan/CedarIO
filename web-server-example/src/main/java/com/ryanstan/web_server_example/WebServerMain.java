package com.ryanstan.web_server_example;

import com.ryanstan.cedario.EventType;
import com.ryanstan.cedario.InitiationDispatcher;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebServerMain {
    public static void main(String[] args) throws IOException {
        InitiationDispatcher dispatcher = InitiationDispatcher.getInstance();

        InetSocketAddress listenSocket = new InetSocketAddress("localhost", 8089);
        RestServiceAcceptor acceptor = new RestServiceAcceptor(listenSocket);
        dispatcher.registerHandler(acceptor, EventType.ACCEPT_EVENT);

        InitiationDispatcher.getInstance().handleEvents();

        /* Not reached */
        return;
    }
}
