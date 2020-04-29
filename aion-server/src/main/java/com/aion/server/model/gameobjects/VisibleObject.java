package com.aion.server.model.gameobjects;

import com.aion.server.model.templates.VisibleObjectTemplate;

import java.io.Serializable;

public abstract class VisibleObject extends AionObject implements Serializable {
    protected VisibleObjectTemplate objectTemplate;

    /**
     * Constructor.
     *
     * @param objId
     * @param objectTemplate
     */
    public VisibleObject(int objId, VisibleObjectTemplate objectTemplate) {
        super(objId);
        this.objectTemplate = objectTemplate;
    }

    private VisibleObject target;

    private boolean isNewSpawn = true;
    /**
     * @return VisibleObject
     */
    public final VisibleObject getTarget() {
        return target;
    }

    /**
     * @param creature
     */
    public void setTarget(VisibleObject creature) {
        target = creature;
    }

    /**
     * @return the objectTemplate
     */
    public VisibleObjectTemplate getObjectTemplate() {
        return objectTemplate;
    }

    /**
     * @param objectTemplate
     *          the objectTemplate to set
     */
    public void setObjectTemplate(VisibleObjectTemplate objectTemplate) {
        this.objectTemplate = objectTemplate;
    }


    public boolean isNewSpawn() {
        return isNewSpawn;
    }

    public void setIsNewSpawn(boolean isNewSpawn) {
        this.isNewSpawn = isNewSpawn;
    }

    @Override
    public String toString() {
        if (objectTemplate == null)
            return super.toString();
        return objectTemplate.getName() + " (" + objectTemplate.getTemplateId() + ")";
    }
}
