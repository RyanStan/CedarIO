package com.ryanstan.cedario;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;
import java.util.*;
import java.nio.channels.SelectionKey;

/**
 * Demultiplex and dispatch EventHandlers in response
 * to client requests.
 *
 * This class implements the Singleton pattern.
 */
public class InitiationDispatcher
{
    private class EventRegistration {
        private EventHandler eventHandler;

        private EventType eventType;

        private EventRegistration(EventHandler eventHandler, EventType eventType) {
            this.eventHandler = eventHandler;
            this.eventType = eventType;
        }
        private EventType getEventType() {
            return eventType;
        }
        private EventHandler getEventHandler() {
            return eventHandler;
        }
    }

    private volatile static InitiationDispatcher initiationDispatcher;

    private Selector selector;

    // During registration, we map the Selection Key associated with an EventRegistration's SelectableChannel to that EventRegistration
    private Map<SelectionKey, EventRegistration> keyEventHandlerMap = new HashMap<>();

    private InitiationDispatcher() throws IOException {
        selector = Selector.open();
    }

    public static InitiationDispatcher getInstance() throws IOException {
        // The double check allows us to avoid synchronization after the initiationDispatcher is created
        if (initiationDispatcher == null) {
            synchronized (InitiationDispatcher.class) {
                if (initiationDispatcher == null) {
                    initiationDispatcher = new InitiationDispatcher();
                }
            }
        }
        return initiationDispatcher;
    }

    public void registerHandler(EventHandler eventHandler, EventType eventType) throws ClosedChannelException {
        SelectableChannel channel = eventHandler.getSelectableChannel();
        int ops = channel.validOps();
        channel.register(selector, ops);
        EventRegistration eventRegistration = new EventRegistration(eventHandler, eventType);
        keyEventHandlerMap.put(channel.register(selector, ops), eventRegistration);
    }

    public void removeHandler(EventHandler eventHandler, EventType eventType) {
        // TODO: implement this.  The original pattern uses these parameters, but I may need to rethink
        // my current data struct choices if I'm going to accept these as well for the remove handler.
    }

    // Entry point into the reactive event loop.  Won't return. Registration of handlers must occur before entering the event loop. TODO: this last bit isn't good, because handlers are going to be created dynamically as connections are accepted
    public void handleEvents() throws IOException {

        for (;;) {
            // Selects a set of keys whose corresponding channels are ready for I/O operations. Blocks until at least one channel is ready.
            selector.select();
            // The selected-key set is the set of keys such that each key's channel was detected to be ready for at least one of the operations identified in the key's interest set
            Set<SelectionKey> selectedKeys = selector.selectedKeys();

            Iterator<SelectionKey> it = selectedKeys.iterator();
            while (it.hasNext()) {
                SelectionKey readyKey = it.next();
                EventRegistration registration = keyEventHandlerMap.get(readyKey);
                if (eventTypeIsReady(readyKey, registration)) {
                    registration.getEventHandler().handleEvent(registration.getEventType());
                }

                // The selector only adds keys to the selectedKeys set, so we must remove them ourselves
                it.remove();
            }

        }
    }

    /*
     * Return true if the EventType associated with the registration is ready on the key.
     */
    private boolean eventTypeIsReady(SelectionKey key, EventRegistration registration) {
        EventType type = registration.getEventType();
        switch (type) {
            case ACCEPT_EVENT:
                if (key.isAcceptable())
                    return true;
                else
                    return false;
            case CONNECT_EVENT:
                if (key.isConnectable())
                    return true;
                else
                    return false;
            case READ_EVENT:
                if (key.isReadable())
                    return true;
                else
                    return false;
            case WRITE_EVENT:
                if (key.isWritable())
                    return true;
                else
                    return false;
            default:
                // This will only be reached if an event is added to eventTypes but we forget to update
                // this method.
                throw new IllegalArgumentException("Did not recognize registered event type");
        }
    }
}
