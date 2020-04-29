package com.aion.server.utils;

import com.aion.server.model.npc.NpcData;

public final class StaticDataUtils {

	private static NpcData npcData;

	private StaticDataUtils() {
		throw new IllegalStateException("Util class");
	}

	public static synchronized void setNpcData(NpcData data) {
		npcData = data;
	}

	public static synchronized NpcData getNpcData() {
		return npcData;
	}
}
