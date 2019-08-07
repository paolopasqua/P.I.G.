package it.unibs.dii.pajc.pig.client.bean.generic;

public class ActionParameter {

    private String id;
    private String description;
    private Class type;

    public ActionParameter(String id, String description, Class type) throws IllegalArgumentException {
        this.id = id;
        this.description = description;
        this.type = type;

        if (this.id == null)
            throw new IllegalArgumentException("ActionParameter(): id can't be null");
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Class getType() {
        return type;
    }

    public boolean isTyped() {
        return type != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof  ActionParameter)
            return id.equals(((ActionParameter)obj).getId());
        else if (obj instanceof  ActionParameterData)
            return ((ActionParameterData)obj).equals(this);

        return super.equals(obj);
    }
}
