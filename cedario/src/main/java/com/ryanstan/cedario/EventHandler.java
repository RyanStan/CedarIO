package com.ryanstan.cedario;

import java.io.IOException;
import java.nio.channels.SelectableChannel;

/*
 * An EventHandler combines an underlying selectable IO Handle along with
 * a callback method to handle events on that Handle.
 */
public interface EventHandler {
    // Hook method that is called back by the InitiationDispatcher to handle events (the callback interface)
    public void handleEvent(EventType eventType) throws IOException;

    // Hook method that returns the underlying NIO Selectable Channel
    public SelectableChannel getSelectableChannel();
}
