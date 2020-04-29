package com.aion.commons.net.packet;

public abstract class BasePacket {
    public static final String TYPE_PATTERN = "[%s] 0x%02X %s";
    private final PacketType packetType;
    private int opcode;

    protected BasePacket(PacketType packetType) {
        this.packetType = packetType;
    }

/*    protected BasePacket(PacketType packetType) {
        this.packetType = packetType;
    }*/

    protected void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    public final int getOpcode() {
        return this.opcode;
    }

    public final PacketType getPacketType() {
        return this.packetType;
    }

    public String getPacketName() {
        return this.getClass().getSimpleName();
    }

    public String toString() {
        return String.format("[%s] 0x%02X %s", this.getPacketType().getName(), this.getOpcode(), this.getPacketName());
    }

}
