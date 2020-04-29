package com.aion.server.engine;

import com.aion.server.model.player.PlayerThread;
import com.aion.server.utils.OnlineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class OnlineEngine implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(OnlineEngine.class);
    private static final OnlineEngine instance = new OnlineEngine();
    private static List<PlayerThread> connections = Collections.synchronizedList(new ArrayList<>());
    private static ServerSocket serverSocket;


    @Override
    public void run() {
        LOG.info("Starting online engine on server [{}]", serverSocket);
        while (!serverSocket.isClosed()) {
            try {
                Thread.sleep(5000);
                checkConnection();
            } catch (InterruptedException e) {
                LOG.error("[{}]", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    static OnlineEngine getInstance(ServerSocket server) {
        serverSocket = server;
        return instance;
    }

    public static synchronized void addConnection(PlayerThread playerThread) {
        connections.add(playerThread);
        OnlineUtils.updateOnline(true);
    }

    private synchronized void checkConnection() {
        if (!connections.isEmpty()) {
            for (Iterator<PlayerThread> playerThread = connections.iterator(); playerThread.hasNext(); ) {
                PlayerThread thread = playerThread.next();
                if (!thread.isAlive()) {
                    OnlineUtils.updateOnline(false);
                    LOG.info("Player [{}] disconnected from server", thread.getName());
                    playerThread.remove();
                }
            }
            LOG.info("Current online is [{}] players", OnlineUtils.getCurrentOnline());
        }
    }
}
