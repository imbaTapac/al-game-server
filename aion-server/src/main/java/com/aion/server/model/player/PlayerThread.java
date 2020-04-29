package com.aion.server.model.player;

import com.aion.commons.net.AttackPacket;
import com.aion.server.engine.OnlineEngine;
import com.aion.server.engine.SpawnHolder;
import com.aion.server.model.npc.Npc;
import com.aion.server.utils.OnlineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


public class PlayerThread extends Thread implements Runnable {
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private Socket playerSocket;
    private Player player;
    private ConnectionState state;


    public PlayerThread(Socket playerSocket, Player player) {
        this.playerSocket = playerSocket;
        this.player = player;
        this.state = ConnectionState.CONNECTED;
    }

    @Override
    public void run() {
        final Logger log = LoggerFactory.getLogger(this.getClass());
        this.player.setNickName(currentThread().getName());
        DataInputStream dis;
        DataOutputStream dos;
        ByteArrayOutputStream bos;
        ObjectOutputStream oos;
        log.info("In thread [{}] trying to get connection", this.getId());
        log.info("Socket [{}]", playerSocket);
        log.info("Player [{}]", player);
        try {
            dis = new DataInputStream(playerSocket.getInputStream());
            dos = new DataOutputStream(playerSocket.getOutputStream());
            this.isRunning.getAndSet(true);
        } catch (IOException e) {
            log.info("IOException occurred");
            return;
        }
        String line;
        while (isRunning.get()) {
            try {
                line = dis.readUTF();
                log.info("Get line from player [{}]", line);
                if (line.equals("GetOnline")) {
                    long online = OnlineUtils.getCurrentOnline();
                    dos.writeLong(online);
                    log.info("Sending current online [{}]", online);
                    dos.flush();
                } else if (line.equals("GetNpcTemplates")) {
                    bos = new ByteArrayOutputStream();
                    oos = new ObjectOutputStream(bos);
                    oos.writeObject(new ArrayList(SpawnHolder.spawnedObject.values()));
                    log.info("Sending [{}] bytes", bos.size());
                    dos.write(bos.toByteArray());
                    dos.flush();
                    oos.close();
                    bos.close();
                } else if (line.equals("AuthPlayer")) {
                    String nick = dis.readUTF();
                    this.player.setNickName(nick);
                    bos = new ByteArrayOutputStream();
                    oos = new ObjectOutputStream(bos);
                    oos.writeObject(this.player);
                    this.state = ConnectionState.AUTHED;
                    OnlineEngine.addConnection(this);
                    log.info("Sending [{}] bytes", bos.size());
                    dos.write(bos.toByteArray());
                    dos.flush();
                    oos.close();
                    bos.close();
                } else if (line.equals("AttackCreature")) {
                    byte[] b = new byte[8192];
                    dis.read(b);
                    log.debug("Received a packet from {} bytes", b.length);
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(b);
                    ObjectInputStream os = new ObjectInputStream(byteArrayInputStream);
                    AttackPacket attackPacket = (AttackPacket) os.readObject();
                    log.info("Received attack packet {}", attackPacket);
                    Npc npc = (Npc) SpawnHolder.spawnedObject.get(attackPacket.getCreatureId());
                    int currentHP = npc.getLifeStats().reduceHp(attackPacket.getAttack(), null);
                    dos.writeInt(currentHP);
                }
            } catch (IOException | ClassNotFoundException e) {
                try {
                    playerSocket.close();
                } catch (IOException e1) {
                    log.error("Error [{}]", e1.getMessage());
                }
                log.error("[{}]", e.getMessage());
                return;
            }
        }
    }

    public boolean getIsRunning() {
        return isRunning.get();
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning.getAndSet(isRunning);
    }

    public synchronized void terminate() {
        if (isRunning.get()) {
            this.interrupt();
        }
    }

    public Socket getPlayerSocket() {
        return playerSocket;
    }

    public void setPlayerSocket(Socket playerSocket) {
        this.playerSocket = playerSocket;
    }
}