package com.aion.server.model.npc;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlRootElement(name = "npc_templates")
public class NpcData implements Serializable {

    @XmlElement(name = "npc_template")
    private List<NpcTemplate> npcs;

    public List<NpcTemplate> getNpcs() {
        return npcs;
    }

    public int size() {
        return npcs.size();
    }
}
