package com.aion.server.model.stats;


import com.aion.server.model.gameobjects.Creature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public abstract class CreatureLifeStats<T extends Creature> implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(CreatureLifeStats.class);

    protected int currentHp;
    protected int currentMp;

    protected boolean alreadyDead = false;

    protected T owner;

    private final Lock hpLock = new ReentrantLock();
    private final Lock mpLock = new ReentrantLock();
    protected final Lock restoreLock = new ReentrantLock();

    protected volatile Future<?> lifeRestoreTask;

    public CreatureLifeStats(T owner, int currentHp, int currentMp) {
        this.owner = owner;
        this.currentHp = currentHp;
        this.currentMp = currentMp;
    }

    public T getOwner() {
        return owner;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getCurrentMp() {
        return currentMp;
    }

    /**
     * @return maxHp of creature according to stats
     */
    public int getMaxHp() {
        int maxHp = this.getOwner().getGameStats().getMaxHp().getCurrent();
        if (maxHp == 0) {
            maxHp = 1;
            log.warn("CHECKPOINT: maxhp is 0 :" + this.getOwner().getGameStats());
        }
        return maxHp;
    }

    /**
     * @return maxMp of creature according to stats
     */
    public int getMaxMp() {
        return this.getOwner().getGameStats().getMaxMp().getCurrent();
    }

    /**
     * @return the alreadyDead There is no setter method cause life stats should be completely renewed on revive
     */
    public boolean isAlreadyDead() {
        return alreadyDead;
    }

    /**
     * This method is called whenever caller wants to absorb creatures's HP
     *
     * @param value
     * @param attacker
     * @return currentHp
     */
    public int reduceHp(int value, Creature attacker) {
        boolean isDied = false;
        hpLock.lock();
        try {
            if (!alreadyDead) {
                int newHp = this.currentHp - value;

                if (newHp < 0) {
                    newHp = 0;
                    alreadyDead = true;
                    isDied = true;
                }
                this.currentHp = newHp;
            }
        } finally {
            hpLock.unlock();
        }
        if (value != 0) {
            onReduceHp();
        }
        if (isDied) {
            //getOwner().getController().onDie(attacker);
        }
        return currentHp;
    }

    /**
     * This method is called whenever caller wants to absorb creatures's HP
     *
     * @param value
     * @return currentMp
     */
    public int reduceMp(int value) {
        mpLock.lock();
        try {
            int newMp = this.currentMp - value;

            if (newMp < 0)
                newMp = 0;

            this.currentMp = newMp;
        } finally {
            mpLock.unlock();
        }
        if (value != 0) {
            onReduceMp();
        }
        return currentMp;
    }

    /**
     * Cancel currently running restore task
     */
    public void cancelRestoreTask() {
        restoreLock.lock();
        try {
            if (lifeRestoreTask != null) {
                lifeRestoreTask.cancel(false);
                lifeRestoreTask = null;
            }
        } finally {
            restoreLock.unlock();
        }
    }

    /**
     * @return true or false
     */
    public boolean isFullyRestoredHpMp() {
        return getMaxHp() == currentHp && getMaxMp() == currentMp;
    }

    /**
     * @return
     */
    public boolean isFullyRestoredHp() {
        return getMaxHp() == currentHp;
    }

    public boolean isFullyRestoredMp() {
        return getMaxMp() == currentMp;
    }

    /**
     * The purpose of this method is synchronize current HP and MP with updated MAXHP and MAXMP stats This method should
     * be called only on creature load to game or player level up
     */
    public void synchronizeWithMaxStats() {
        int maxHp = getMaxHp();
        if (currentHp != maxHp)
            currentHp = maxHp;
        int maxMp = getMaxMp();
        if (currentMp != maxMp)
            currentMp = maxMp;
    }

    /**
     * The purpose of this method is synchronize current HP and MP with MAXHP and MAXMP when max stats were decreased
     * below current level
     */
    public void updateCurrentStats() {
        int maxHp = getMaxHp();
        if (maxHp < currentHp)
            currentHp = maxHp;

        int maxMp = getMaxMp();
        if (maxMp < currentMp)
            currentMp = maxMp;

        if (!isFullyRestoredHpMp()) {
            //triggerRestoreTask();
        }
    }

    /**
     * @return HP percentage 0 - 100
     */
    public int getHpPercentage() {
        return (int) (100L * currentHp / getMaxHp());
    }

    /**
     * @return MP percentage 0 - 100
     */
    public int getMpPercentage() {
        return 100 * currentMp / getMaxMp();
    }

    protected abstract void onReduceMp();

    protected abstract void onReduceHp();

    public int getMaxFp() {
        return 0;
    }

    /**
     * @return
     */
    public int getCurrentFp() {
        return 0;
    }

    /**
     * Cancel all tasks when player logout
     */
    public void cancelAllTasks() {
        cancelRestoreTask();
    }

    /**
     * This method can be used for Npc's to fully restore its HP and remove dead state of lifestats
     *
     * @param hpPercent
     */
    public void setCurrentHpPercent(int hpPercent) {
        hpLock.lock();
        try {
            int maxHp = getMaxHp();
            this.currentHp = (int) ((long) maxHp * hpPercent / 100);

            if (this.currentHp > 0)
                this.alreadyDead = false;
        } finally {
            hpLock.unlock();
        }
    }

    /**
     * @param hp
     */
    public void setCurrentHp(int hp) {
        boolean hpNotAtMaxValue = false;
        hpLock.lock();
        try {
            this.currentHp = hp;

            if (this.currentHp > 0)
                this.alreadyDead = false;

            if (this.currentHp < getMaxHp())
                hpNotAtMaxValue = true;
        } finally {
            hpLock.unlock();
        }
        if (hpNotAtMaxValue) {
            onReduceHp();
        }
    }

    public int setCurrentMp(int value) {
        mpLock.lock();
        try {
            int newMp = value;

            if (newMp < 0)
                newMp = 0;

            this.currentMp = newMp;
        } finally {
            mpLock.unlock();
        }
        onReduceMp();
        return currentMp;
    }

    /**
     * This method can be used for Npc's to fully restore its MP
     *
     * @param mpPercent
     */
    public void setCurrentMpPercent(int mpPercent) {
        mpLock.lock();
        try {
            int maxMp = getMaxMp();
            this.currentMp = maxMp * mpPercent / 100;
        } finally {
            mpLock.unlock();
        }
    }

}
