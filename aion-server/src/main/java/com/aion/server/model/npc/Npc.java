package com.aion.server.model.npc;

import com.aion.server.model.gameobjects.Creature;
import com.aion.server.model.player.Player;
import com.aion.server.model.stats.container.NpcGameStats;
import com.aion.server.model.stats.container.NpcLifeStats;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

public class Npc extends Creature implements Serializable {

    private boolean isQuestBusy = false;
    private Player questPlayer = null;
    private long lastShoutedSeconds;

    public Npc(int objId, NpcTemplate objectTemplate) {
        super(objId, objectTemplate);
        setupStatContainers();
        lastShoutedSeconds = System.currentTimeMillis() / 1000;
    }


    protected void setupStatContainers() {
        setGameStats(new NpcGameStats(this));
        setLifeStats(new NpcLifeStats(this));
    }

    @Override
    public NpcTemplate getObjectTemplate() {
        return (NpcTemplate) objectTemplate;
    }

    @Override
    public String getName() {
        return getObjectTemplate().getName();
    }

    public int getNpcId() {
        return getObjectTemplate().getTemplateId();
    }

    @Override
    public byte getLevel() {
        return getObjectTemplate().getLevel();
    }

    @Override
    public NpcLifeStats getLifeStats() {
        return (NpcLifeStats) super.getLifeStats();
    }

    @Override
    public NpcGameStats getGameStats() {
        return (NpcGameStats) super.getGameStats();
    }


    public int getAggroRange() {
        return getObjectTemplate().getAggroRange();
    }


    @Override
    public boolean isEnemy(Creature creature) {
        return creature.isEnemyFrom(this);
    }

    public boolean getIsQuestBusy() {
        return isQuestBusy;
    }

    public void setIsQuestBusy(boolean busy) {
        isQuestBusy = busy;
    }

    public Player getQuestPlayer() {
        return questPlayer;
    }

    public void setQuestPlayer(Player player) {
        questPlayer = player;
    }

    @Override
    public boolean isAttackableNpc() {
        return getObjectTemplate().getNpcType() == NpcType.ATTACKABLE;
    }

    /**
     * @return Name of the Creature who summoned this Npc
     */
    public String getCreatorName() {
        return StringUtils.EMPTY;
    }

    /**
     * @return UniqueId of the Creature who summoned this Npc
     */
    public int getCreatorId() {
        return 0;
    }

    /**
     * @return
     */
    public Creature getCreator() {
        return null;
    }

    @Override
    public Race getRace() {
        return this.getObjectTemplate().getRace();
    }


    public NpcType getNpcType() {
        return getObjectTemplate().getNpcType();
    }


    @Override
    public String toString() {
        return "Name=" + getName() +
                ", Race=" + getRace() +
                ", Rank=" + getObjectTemplate().getRank().toString() +
                ", MaxHP=" + getObjectTemplate().getStatsTemplate().getMaxHp();
    }
}
