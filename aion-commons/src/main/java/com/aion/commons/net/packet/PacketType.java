package com.aion.commons.net.packet;

public enum  PacketType {
    SERVER("SERVER"),
    CLIENT("CLIENT");

    private final String name;

    private PacketType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
