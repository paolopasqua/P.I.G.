package it.unibs.dii.pajc.pig.client.bean.generic;

public class Action {

    protected String id;
    protected String description;
    protected Action terminationAction;

    public Action(String id) throws IllegalArgumentException {
        this.id = id;

        if (this.id == null)
            throw  new IllegalArgumentException("Action(): id can't be null");

        this.description = null;
        this.terminationAction = null;
    }

    public Action(String id, String description) throws IllegalArgumentException {
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

    public Action getTerminationAction() {
        return terminationAction;
    }

    public void setTerminationAction(Action action) {
        this.terminationAction = action;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Action)
            return id.equals(((Action)obj).getID());

        return super.equals(obj);
    }
}
