package com.aion.commons.net;

import java.io.Serializable;

public class AttackPacket implements Serializable {
	private Integer creatureId;
	private Integer attack;
	private String attacker;

	public AttackPacket(Integer creatureId, Integer attack, String attacker) {
		this.creatureId = creatureId;
		this.attack = attack;
		this.attacker = attacker;
	}

	public String getAttacker() {
		return attacker;
	}

	public void setAttacker(String attacker) {
		this.attacker = attacker;
	}

	public Integer getCreatureId() {
		return creatureId;
	}

	public void setCreatureId(Integer creatureId) {
		this.creatureId = creatureId;
	}

	public Integer getAttack() {
		return attack;
	}

	public void setAttack(Integer attack) {
		this.attack = attack;
	}

	@Override
	public String toString() {
		return "AttackPacket{" +
				"creatureId=" + creatureId +
				", attack=" + attack +
				", attacker='" + attacker + '\'' +
				'}';
	}
}
