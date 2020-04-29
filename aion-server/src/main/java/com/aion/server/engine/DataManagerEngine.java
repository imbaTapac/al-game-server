package com.aion.server.engine;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aion.server.model.npc.NpcData;
import com.aion.server.utils.StaticDataUtils;

public class DataManagerEngine {
	public static final Logger LOG = LoggerFactory.getLogger(DataManagerEngine.class);

	private static NpcData NPC_DATA;

	public static DataManagerEngine getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {
		protected static final DataManagerEngine instance = new DataManagerEngine();
	}

	private DataManagerEngine() {
		LOG.info("Loading Static data");
		long start = System.currentTimeMillis();
		try {
			unmarshaller();
		} catch(JAXBException e) {
			LOG.error("[{}]", e);
		}
		LOG.info("Loaded :[{}] npcs", NPC_DATA.getNpcs().size());
		long time = System.currentTimeMillis() - start;
		LOG.info("Loading finished with time [{}] ms", time);
	}

	private void unmarshaller() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(NpcData.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		NPC_DATA = (NpcData) unmarshaller.unmarshal(new File("aion-server/data/static_data/npcs/npc_templates.xml"));
		StaticDataUtils.setNpcData(NPC_DATA);
	}

	public static NpcData getNpcs(){
		return NPC_DATA;
	}
}
