package com.aion.game.listener;

import com.aion.game.controller.LoginFormController;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;


public class ServerListener extends Task {
    private static final Logger LOG = LoggerFactory.getLogger(ServerListener.class);

    private InetAddress ip;
    private int port;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    private static final ServerListener serverListener = new ServerListener();

    private ServerListener() {
        try {
            new Thread(call()).start();
        } catch (Exception e) {
            LOG.error("[{}]", e);
        }
    }

    @Override
    public Task<Void> call() {
        return new Task<Void>() {
            @Override
            protected Void call() {
                loadProperties();
                ServerListener.this.run();
                return null;
            }
        };
    }

    @Override
    public void run() {
        try {
            LOG.info("Trying to connect");
            socket = new Socket(ip, port);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            if (socket.isConnected()) {
                LOG.info("Successfully connected to game-server");
            }
        } catch (IOException e) {
            LOG.error("[{}]", e);
        }
        while (!socket.isClosed()) {
            try {
                Thread.sleep(5000);
                if (LoginFormController.getInstance().isActiveForm()) {
                    LoginFormController.getInstance().handleServerStatus(socket);
                    LoginFormController.getInstance().handleOnline(socket);
                }
            } catch (InterruptedException e) {
                LOG.error("[{}]", e);
                Thread.currentThread().interrupt();
            }
            LOG.info("Keeping Player thread on");
            if (!socket.isConnected()) {
                LOG.info("Lost connection to game-server");
            }
        }
    }

    public static ServerListener getInstance() {
        return serverListener;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public synchronized void terminate() {
        LOG.info("Player disconnect.\nServer listener is terminating");
        try {
            socket.close();
            dos.close();
            dis.close();
        } catch (IOException e) {
            LOG.error("[{}]", e);
        }
    }

    private void loadProperties() {
        InputStream is = ServerListener.class.getClassLoader().getResourceAsStream("client.properties");
        try {
            Properties properties = new Properties();
            properties.load(is);
            if (properties.getProperty("server.address").length() > 0) {
                ip = InetAddress.getByName(properties.getProperty("server.address"));
            }
            if (properties.getProperty("server.port").length() > 0) {
                port = Integer.valueOf(properties.getProperty("server.port"));
            }
        } catch (IOException e) {
            LOG.error("{}",e);
        }
    }
}
