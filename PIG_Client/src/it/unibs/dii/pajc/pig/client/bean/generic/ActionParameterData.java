package it.unibs.dii.pajc.pig.client.bean.generic;

public class ActionParameterData {

    private String id;
    private Object data;

    public ActionParameterData(ActionParameter parameter, Object data) throws IllegalArgumentException {
        if (parameter.isTyped() && !parameter.getType().isInstance(data))
            throw new IllegalArgumentException("ActionParameterData(): action parameter data type not equals the expected type");

        this.id = parameter.getId();
        this.data = data;
    }

    public String getParameterId() {
        return id;
    }

    public Object getData() {
        return data;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof  ActionParameterData)
            return id.equals(((ActionParameterData)obj).getParameterId());
        else if (obj instanceof  ActionParameter)
            return ((ActionParameter)obj).equals(this);

        return super.equals(obj);
    }
}
