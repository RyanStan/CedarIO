package com.ryanstan.cedario;

/**
 * Types of events handled by the
 * InitiationDispatcher.
 *
 * These correspond to the valid operations on a Java SelectionKey.
 */
public enum EventType {
    ACCEPT_EVENT,
    CONNECT_EVENT,
    READ_EVENT,
    WRITE_EVENT
}
