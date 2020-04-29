package com.aion.server.model.player;

public enum ConnectionState {
    /**
     * client just connect
     */
    CONNECTED,
    /**
     * client is authenticated
     */
    AUTHED,
    /**
     * client entered world.
     */
    IN_GAME
}

