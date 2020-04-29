package com.aion.server.model.npc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.aion.server.model.templates.StatsTemplate;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "npc_stats_template")
public class NpcStatsTemplate extends StatsTemplate {

	@XmlAttribute(name = "run_speed_fight")
	private float runSpeedFight;
	@XmlAttribute(name = "pdef")
	private int pdef;
	@XmlAttribute(name = "mdef")
	private int mdef;
	@XmlAttribute(name = "mresist")
	private int mresist;
	@XmlAttribute(name = "crit")
	private int crit;
	@XmlAttribute(name = "accuracy")
	private int accuracy;
	@XmlAttribute(name = "power")
	private int power;
	@XmlAttribute(name = "maxXp")
	private int maxXp;
	@XmlAttribute(name = "maxHp")
	private int maxHp;

	public float getRunSpeedFight() {
		return runSpeedFight;
	}

	/**
	 * @return the pdef
	 */
	public int getPdef() {
		return pdef;
	}

	/**
	 * @return the mdef
	 */
	public float getMdef() {
		return mdef;
	}

	/**
	 * @return the mresist
	 */
	public int getMresist() {
		return mresist;
	}

	/**
	 * @return the crit
	 */
	public float getCrit() {
		return crit;
	}

	/**
	 * @return the accuracy
	 */
	public float getAccuracy() {
		return accuracy;
	}

	/**
	 * @return the power
	 */
	public int getPower() {
		return power;
	}

	/**
	 * @return the maxXp
	 */
	public int getMaxXp() {
		return maxXp;
	}

	public int getMaxHp() {
		return super.getMaxHp();
	}


	@Override
	public String toString() {
		return "stats:" +
				"runSpeedFight=" + runSpeedFight +
				", pdef=" + pdef +
				", mdef=" + mdef +
				", mresist=" + mresist +
				", crit=" + crit +
				", accuracy=" + accuracy +
				", power=" + power +
				", maxXp=" + maxXp +
				", maxHp=" + maxHp +
				'}';
	}
}