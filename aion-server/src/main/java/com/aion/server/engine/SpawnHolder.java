package com.aion.server.engine;

import com.aion.server.model.gameobjects.VisibleObject;

import java.util.HashMap;
import java.util.Map;

public final class SpawnHolder {

    public static Map<Integer, VisibleObject> spawnedObject = new HashMap<>();

    public static SpawnHolder getInstance() {
        return SpawnHolder.SingletonHolder.instance;
    }

    private static class SingletonHolder {
        protected static final SpawnHolder instance = new SpawnHolder();

    }

    private SpawnHolder() {
    }
}
