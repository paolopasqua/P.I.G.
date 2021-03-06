package it.unibs.dii.pajc.pig.client.bean.abstraction;

import it.unibs.dii.pajc.pig.client.bean.generic.Action;
import it.unibs.dii.pajc.pig.client.view.renderer.ComponentDrawer;

public abstract class Device {

    protected String id;
    protected String description;
    protected Action[] actions;
    protected ComponentDrawer drawer;

    public abstract void setStatus(Status status);
    public abstract Status getStatus();

    public Device(String id) throws IllegalArgumentException {
        this.id = id;
        this.drawer = null;

        if (this.id == null)
            throw  new IllegalArgumentException("Device(): id can't be null");
    }

    public Device(String id, String description) throws IllegalArgumentException {
        this(id);
        setDescription(description);
    }

    public String getID() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Action[] getActions() {
        return actions.clone();
    }

    public ComponentDrawer getDrawer() {
        return drawer;
    }

    @Override
    public boolean equals(Object obj) {
        if (Device.class.isAssignableFrom(obj.getClass()))
            return id.equals(((Device)obj).getID());

        return super.equals(obj);
    }
}
