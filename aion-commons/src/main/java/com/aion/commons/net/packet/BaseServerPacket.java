package com.aion.commons.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BaseServerPacket extends BasePacket{
    private DataInputStream dis;
    private DataOutputStream dos;

    protected BaseServerPacket(PacketType packetType) {
        super(PacketType.SERVER);
    }

    public DataInputStream getDis() {
        return dis;
    }

    public void setDis(DataInputStream dis) {
        this.dis = dis;
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }

    protected final void writeL(long l){
        try {
            this.dos.writeLong(l);
            this.dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
