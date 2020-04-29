package com.aion.server.engine;

import com.aion.server.model.npc.Npc;
import com.aion.server.model.npc.NpcTemplate;
import com.aion.server.model.templates.VisibleObjectTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpawnEngine {
    private final static Logger LOG = LoggerFactory.getLogger(SpawnEngine.class);

    public static SpawnEngine getInstance() {
        return SpawnEngine.SingletonHolder.instance;
    }

    private static class SingletonHolder {
        protected static final SpawnEngine instance = new SpawnEngine();

    }

    private SpawnEngine(){
        LOG.info("Start spawning all NPC's");
        SpawnHolder.getInstance();
        SpawnEngine.spawnAll();
        LOG.info("Spawned {} npcs",SpawnHolder.spawnedObject.size());
    }

    /**
     * Spawn all NPC's from templates
     */
    public static void spawnAll() {
        for (VisibleObjectTemplate vot : DataManagerEngine.getNpcs().getNpcs()) {
            Npc npc = new Npc(vot.getNameId(),(NpcTemplate) vot);
            SpawnHolder.spawnedObject.put(npc.getNpcId(),npc);
        }
    }


}
