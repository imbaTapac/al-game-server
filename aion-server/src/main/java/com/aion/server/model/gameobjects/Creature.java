package com.aion.server.model.gameobjects;

import com.aion.server.model.npc.Npc;
import com.aion.server.model.npc.Race;
import com.aion.server.model.player.Player;
import com.aion.server.model.state.CreatureState;
import com.aion.server.model.stats.CreatureGameStats;
import com.aion.server.model.stats.CreatureLifeStats;
import com.aion.server.model.templates.VisibleObjectTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public abstract class Creature extends VisibleObject implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Creature.class);

    private CreatureLifeStats<? extends Creature> lifeStats;
    private CreatureGameStats<? extends Creature> gameStats;

    private int state = CreatureState.ACTIVE.getId();

    private int transformedModelId;

    private int isAdminNeutral = 0;
    private int isAdminEnmity = 0;


    private boolean oneTimeBoostSkillCritical = false;

    private int skillNumber;
    private int attackedCount;


    public Creature(int objId, VisibleObjectTemplate objectTemplate) {
        super(objId, objectTemplate);
    }

    /**
     * @return the lifeStats
     */
    public CreatureLifeStats<? extends Creature> getLifeStats() {
        return lifeStats;
    }

    /**
     * @param lifeStats the lifeStats to set
     */
    public void setLifeStats(CreatureLifeStats<? extends Creature> lifeStats) {
        this.lifeStats = lifeStats;
    }

    /**
     * @return the gameStats
     */
    public CreatureGameStats<? extends Creature> getGameStats() {
        return gameStats;
    }

    /**
     * @param gameStats the gameStats to set
     */
    public void setGameStats(CreatureGameStats<? extends Creature> gameStats) {
        this.gameStats = gameStats;
    }

    public abstract byte getLevel();

    public int getSkillNumber() {
        return skillNumber;
    }

    public void setSkillNumber(int skillNumber) {
        this.skillNumber = skillNumber;
    }

    public int getAttackedCount() {
        return this.attackedCount;
    }

    public void incrementAttackedCount() {
        this.attackedCount++;
    }

    public void clearAttackedCount() {
        attackedCount = 0;
    }

    public int getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(CreatureState state) {
        this.state |= state.getId();
    }

    /**
     * @param state taken usually from templates
     */
    public void setState(int state) {
        this.state = state;
    }

    public void unsetState(CreatureState state) {
        this.state &= ~state.getId();
    }

    public boolean isInState(CreatureState state) {
        int isState = this.state & state.getId();

        if (isState == state.getId())
            return true;

        return false;
    }


    /**
     * @return the transformedModelId
     */
    public int getTransformedModelId() {
        return transformedModelId;
    }

    /**
     * @param transformedModelId the transformedModelId to set
     */
    public void setTransformedModelId(int transformedModelId) {
        this.transformedModelId = transformedModelId;
    }


    /**
     * PacketBroadcasterMask
     */
    private volatile byte packetBroadcastMask;


    /**
     * Broadcast getter.
     */
    public final byte getPacketBroadcastMask() {
        return packetBroadcastMask;
    }


    /**
     * Double dispatch like method
     *
     * @param creature
     * @return
     */
    public boolean isEnemy(Creature creature) {
        return creature.isEnemyFrom(this);
    }

    /**
     * @param creature
     */
    public boolean isEnemyFrom(Creature creature) {
        return false;
    }

    /**
     * @param player
     * @return
     */
    public boolean isEnemyFrom(Player player) {
        return false;
    }

    /**
     * @param npc
     * @return
     */
    public boolean isEnemyFrom(Npc npc) {
        return false;
    }

    /**
     * Double dispatch like method
     *
     * @param creature
     * @return
     */
    public boolean isAggressiveTo(Creature creature) {
        return creature.isAggroFrom(this);
    }

    /**
     * @param creature
     * @return
     */
    public boolean isAggroFrom(Creature creature) {
        return false;
    }

    /**
     * @param npc
     * @return
     */
    public boolean isAggroFrom(Npc npc) {
        return false;
    }

    /**
     * @param npc
     * @return
     */
    public boolean isHostileFrom(Npc npc) {
        return false;
    }

    /**
     * @param npc
     */
    public boolean isSupportFrom(Npc npc) {
        return false;
    }

    /**
     * @param npc
     */
    public boolean isFriendFrom(Npc npc) {
        return false;
    }


    /**
     * For summons and different kind of servants<br>
     * it will return currently acting player.<br>
     * This method is used for duel and enemy relations,<br>
     * rewards<br>
     *
     * @return Master of this creature or self
     */
    public Creature getMaster() {
        return this;
    }

    /**
     * For summons it will return summon object and for <br>
     * servants - player object.<br>
     * Used to find attackable target for npcs.<br>
     *
     * @return acting master - player in case of servants
     */
    public Creature getActingCreature() {
        return this;
    }


    /**
     * @return isAdminNeutral value
     */
    public int getAdminNeutral() {
        return isAdminNeutral;
    }

    /**
     * @param newValue
     */
    public void setAdminNeutral(int newValue) {
        isAdminNeutral = newValue;
    }

    /**
     * @param newValue
     */
    public void setOneTimeBoostSkillCritical(boolean value) {
        this.oneTimeBoostSkillCritical = value;
    }

    /**
     * @return the BoostSkillCritical status
     */
    public boolean isOneTimeBoostSkillCritical() {
        return this.oneTimeBoostSkillCritical;
    }

    /**
     * @return isAdminEnmity value
     */
    public int getAdminEnmity() {
        return isAdminEnmity;
    }

    /**
     * @param newValue
     */
    public void setAdminEnmity(int newValue) {
        isAdminEnmity = newValue;
    }

    /**
     * @return
     */
    public boolean isAttackableNpc() {
        return false;
    }


    public byte isPlayer() {
        return 0;
    }

    public Race getRace() {
        return Race.NONE;
    }

}
