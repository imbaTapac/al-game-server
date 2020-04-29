package com.aion.server.model.data;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aion.server.engine.DataManagerEngine;
import com.aion.server.model.npc.NpcData;

@XmlRootElement(name = "ai_static_data")
@XmlAccessorType(XmlAccessType.NONE)
public class StaticData {

	@XmlElement(name = "npc_templates")
	public NpcData npcData;

	private void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		DataManagerEngine.LOG.info("Loaded [{}] npc templates",npcData.getNpcs().size());
	}
}
