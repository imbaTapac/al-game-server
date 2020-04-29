package com.aion.server.engine;

import com.aion.server.model.player.Class;
import com.aion.server.model.player.Player;
import com.aion.server.model.player.PlayerThread;
import com.aion.server.model.player.Race;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerEngine implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(ServerEngine.class);
    private ServerSocket serverSocket;
    private int serverPort = 1978;
    private volatile boolean running = false;
    private volatile boolean isShutdown = false;
    private static final ServerEngine instance = new ServerEngine();

    private Thread runngingThread = null;

    private ServerEngine() {
        run();
    }

    @Override
    public void run() {
        synchronized (this) {
            this.runngingThread = Thread.currentThread();
        }
        try {
            LOG.info("Starting game-server on serverPort [{}]", serverPort);
            serverSocket = new ServerSocket(serverPort);
            new Thread(OnlineEngine.getInstance(serverSocket)).start();
            DataManagerEngine.getInstance();
            SpawnEngine.getInstance();
        } catch (IOException e) {
            LOG.error("{}", e);
        }
        while (!isShutdown) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                LOG.info("New connection detected: [{}] [{}]", serverSocket.getLocalSocketAddress(), clientSocket.getRemoteSocketAddress());
            } catch (IOException e) {
                LOG.error("I/O error: [{}]", e);
            }

            PlayerThread playerThread = new PlayerThread(clientSocket, new Player(1L, " ", 55, 20000L, Race.ELYOS, Class.SORCERER, 2500, 18.0));
            LOG.info("Starting player thread");
            playerThread.start();
            LOG.info("Started player thread [{}]", playerThread.getId());
        }
    }

    private synchronized void terminate() {
        if (isShutdown) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> LOG.info("Shutdown Hook is running")));
            LOG.info("Server is terminating");
            try {
                serverSocket.close();
            } catch (IOException e) {
                LOG.error("[{}]", e);
            }
        }
    }

    public static ServerEngine getInstance() {
        return instance;
    }


    private void setShutdown(boolean shutdown) {
        this.isShutdown = shutdown;
    }

    public int getServerPort() {
        return this.serverSocket.getLocalPort();
    }

    public boolean isRunning() {
        return this.running;
    }
}
