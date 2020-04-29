package com.aion.server.model.player;

import java.io.Serializable;

public class Player implements Serializable {
	private Long id;
	private String nickName;
	private int level;
	private Long currentExp;
	private Race race;
	private Class playerClass;
	private Integer attack;
	private Double criticalChance;

	public Player() {
	}

	public Player(Long id, String nickName,int level, Long currentExp, Race race, Class playerClass, Integer attack, Double criticalChance) {
		this.id = id;
		this.nickName = nickName;
		this.level = level;
		this.currentExp = currentExp;
		this.race = race;
		this.playerClass = playerClass;
		this.attack = attack;
		this.criticalChance = criticalChance;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Long getCurrentExp() {
		return currentExp;
	}

	public void setCurrentExp(Long currentExp) {
		this.currentExp = currentExp;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	public Class getPlayerClass() {
		return playerClass;
	}

	public void setPlayerClass(Class playerClass) {
		this.playerClass = playerClass;
	}

	public Integer getAttack() {
		return attack;
	}

	public void setAttack(Integer attack) {
		this.attack = attack;
	}

	public Double getCriticalChance() {
		return criticalChance;
	}

	public void setCriticalChance(Double criticalChance) {
		this.criticalChance = criticalChance;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Override
	public String toString() {
		return "Player{" +
				"id=" + id +
				", nickName=" + nickName +
				", level=" + level +
				", currentExp=" + currentExp +
				", race=" + race +
				", playerClass=" + playerClass +
				", attack=" + attack +
				", criticalChance=" + criticalChance +
				'}';
	}
}
