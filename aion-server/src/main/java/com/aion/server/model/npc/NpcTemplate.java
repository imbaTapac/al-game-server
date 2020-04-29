package com.aion.server.model.npc;

import com.aion.server.model.templates.VisibleObjectTemplate;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "npc_template")
public class NpcTemplate extends VisibleObjectTemplate implements Serializable {
    private int npcId;
    @XmlAttribute(name = "level", required = true)
    private byte level;
    @XmlAttribute(name = "name_id", required = true)
    private int nameId;
    @XmlAttribute(name = "title_id")
    private int titleId;
    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "rank")
    private NpcRank npcRank;
    @XmlAttribute(name = "race")
    private Race race;
    @XmlAttribute(name = "height")
    private float height = 1;
    @XmlAttribute(name = "talk_distance")
    private int talkDistance = 2;
    @XmlAttribute(name = "npc_type", required = true)
    private NpcType npcType;

    @XmlElement(name = "stats")
    private NpcStatsTemplate statsTemplate;

    @XmlAttribute(name = "srange")
    private int aggrorange;
    @XmlAttribute(name = "arange")
    private int attackRange;
    @XmlAttribute(name = "arate")
    private int attackRate;
    @XmlAttribute(name = "adelay")
    private int attackDelay;
    @XmlAttribute(name = "hpgauge")
    private int hpGauge;
    @XmlAttribute
    private int state;
    @XmlAttribute(name = "talk_delay")
    private int talk_delay;


    public int getTitleId() {
        return titleId;
    }

    public float getHeight() {
        return height;
    }


    public byte getLevel() {
        return level;
    }

    public NpcStatsTemplate getStatsTemplate() {
        return statsTemplate;
    }

    public void setStatsTemplate(NpcStatsTemplate statsTemplate) {
        this.statsTemplate = statsTemplate;
    }

    @Override
    public String toString() {
        return "Npc Template id: " + npcId + " name: " + name + " rank: " + npcRank + " race:" + race
                + " HP:" + statsTemplate.getMaxHp();
    }

    @SuppressWarnings("unused")
    @XmlID
    @XmlAttribute(name = "npc_id", required = true)
    private void setXmlUid(String uid) {
        /*
         * This method is used only by JAXB unmarshaller. I couldn't set
         * annotations at field, because ID must be a string.
         */
        npcId = Integer.parseInt(uid);
    }


    public int getAggroRange() {
        return aggrorange;
    }

    public int getShoutRange() {
        if (aggrorange < 10)
            return 10;
        return aggrorange;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public int getAttackRate() {
        return attackRate;
    }

    public int getAttackDelay() {
        return attackDelay;
    }

    public int getHpGauge() {
        return hpGauge;
    }

    public final int getTalkDistance() {
        return talkDistance;
    }

    public int getTalkDelay() {
        return talk_delay;
    }

    public int getNpcId() {
        return npcId;
    }

    public void setNpcId(int npcId) {
        this.npcId = npcId;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public int getNameId() {
        return nameId;
    }

    public void setNameId(int nameId) {
        this.nameId = nameId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    @Override
    public int getTemplateId() {
        return npcId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setTalkDistance(int talkDistance) {
        this.talkDistance = talkDistance;
    }

    public NpcType getNpcType() {
        return npcType;
    }

    public void setNpcType(NpcType npcType) {
        this.npcType = npcType;
    }


    public int getAggrorange() {
        return aggrorange;
    }

    public void setAggrorange(int aggrorange) {
        this.aggrorange = aggrorange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public void setAttackRate(int attackRate) {
        this.attackRate = attackRate;
    }

    public void setAttackDelay(int attackDelay) {
        this.attackDelay = attackDelay;
    }

    public void setHpGauge(int hpGauge) {
        this.hpGauge = hpGauge;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getTalk_delay() {
        return talk_delay;
    }

    public void setTalk_delay(int talk_delay) {
        this.talk_delay = talk_delay;
    }

    public NpcRank getRank() {
        return npcRank;
    }

    public void setNpcRank(NpcRank npcRank) {
        this.npcRank = npcRank;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }
}

