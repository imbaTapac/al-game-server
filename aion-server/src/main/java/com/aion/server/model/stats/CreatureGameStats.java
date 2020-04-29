
package com.aion.server.model.stats;

import com.aion.server.model.gameobjects.Creature;
import com.aion.server.model.stats.container.StatEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public abstract class CreatureGameStats<T extends Creature> implements Serializable {

    protected static final Logger log = LoggerFactory.getLogger(CreatureGameStats.class);

    private static final int ATTACK_MAX_COUNTER = Integer.MAX_VALUE;
    private long lastGeoUpdate = 0;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private int attackCounter = 0;
    protected T owner = null;

    private Stat2 cachedHPStat;
    private Stat2 cachedMPStat;

    protected CreatureGameStats(T owner) {
        this.owner = owner;
    }

    /**
     * @return the atcount
     */
    public int getAttackCounter() {
        return attackCounter;
    }


    protected void setAttackCounter(int attackCounter) {
        if (attackCounter <= 0) {
            this.attackCounter = 1;
        }
        else {
            this.attackCounter = attackCounter;
        }
    }

    public void increaseAttackCounter() {
        if (attackCounter == ATTACK_MAX_COUNTER) {
            this.attackCounter = 1;
        }
        else {
            this.attackCounter++;
        }
    }

    public int getPositiveStat(StatEnum statEnum, int base) {
        Stat2 stat = getStat(statEnum, base);
        int value = stat.getCurrent();
        return value > 0 ? value : 0;
    }


    public Stat2 getStat(StatEnum statEnum, int base) {
        Stat2 stat = new AdditionStat(statEnum, base, (Creature) owner);
        return stat;
    }

    public Stat2 getStat(StatEnum statEnum, int base, float bonusRate) {
        Stat2 stat = new AdditionStat(statEnum, base, (Creature) owner, bonusRate);
        return stat;
    }


    public abstract Stat2 getMaxHp();

    public abstract Stat2 getMaxMp();

    public abstract Stat2 getAttackSpeed();

    public abstract Stat2 getMovementSpeed();

    public abstract Stat2 getAttackRange();

    public abstract Stat2 getPDef();

    public abstract Stat2 getMResist();

    public abstract Stat2 getPower();

    public abstract Stat2 getHealth();

    public abstract Stat2 getAccuracy();

    public abstract Stat2 getAgility();

    public abstract Stat2 getKnowledge();

    public abstract Stat2 getWill();

    public abstract Stat2 getEvasion();

    public abstract Stat2 getParry();

    public abstract Stat2 getBlock();

    public abstract Stat2 getMainHandPAttack();

    public abstract Stat2 getMainHandPCritical();

    public abstract Stat2 getMainHandPAccuracy();

    public abstract Stat2 getMAttack();

    public abstract Stat2 getMBoost();

    public abstract Stat2 getMAccuracy();

    public abstract Stat2 getMCritical();

    public abstract Stat2 getHpRegenRate();

    public abstract Stat2 getMpRegenRate();


    /**
     * Send packet about stats info
     */
    public void updateStatInfo() {
    }

    /**
     * Send packet about speed info
     */
    public void updateSpeedInfo() {
    }

    /**
     * @return
     */
    public boolean checkGeoNeedUpdate() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastGeoUpdate > 600) {
            lastGeoUpdate = currentTime;
            return true;
        }
        return false;
    }

    /**
     * Perform additional calculations after effects added/removed<br>
     * This method will be called outside of stats lock.
     */
    protected void onStatsChange() {
        checkHPStats();
        checkMPStats();
    }

    private void checkHPStats() {
        Stat2 oldHP = cachedHPStat;
        cachedHPStat = null;
        Stat2 newHP = this.getMaxHp();
        cachedHPStat = newHP;
        if (oldHP == null)
            return;
        if (oldHP.getCurrent() != newHP.getCurrent()) {
            float percent = 1f * newHP.getCurrent() / oldHP.getCurrent();
            owner.getLifeStats().setCurrentHp(Math.round(owner.getLifeStats().getCurrentHp() * percent));
        }
    }

    private void checkMPStats() {
        Stat2 oldMP = cachedMPStat;
        cachedMPStat = null;
        Stat2 newMP = this.getMaxMp();
        cachedMPStat = newMP;
        if (oldMP == null)
            return;
        if (oldMP.getCurrent() != newMP.getCurrent()) {
            float percent = 1f * newMP.getCurrent() / oldMP.getCurrent();
            owner.getLifeStats().setCurrentMp(Math.round(owner.getLifeStats().getCurrentMp() * percent));
        }
    }
}
