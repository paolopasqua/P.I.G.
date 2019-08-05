package it.unibs.dii.pajc.pig.client.utility;

import javax.swing.event.ListDataEvent;

public class ListMultiTypeEvent extends ListDataEvent {

    private Class elementType;

    public <T extends Object> ListMultiTypeEvent(Object source, int type, Class<T> elementType, int index0, int index1) {
        super(source, type, index0, index1);

        this.elementType = elementType;
    }

    public <T extends Object> ListMultiTypeEvent(ListDataEvent evt, Class<T> elementType) {
        this(evt.getSource(), evt.getType(), elementType, evt.getIndex0(), evt.getIndex1());
    }

    public Class getElementType() {
        return elementType;
    }

    @Override
    public String toString() {
        String var1000 = this.getClass().getName();
        return var1000 + "[type=" + getType() + ",element_type=" + getElementType() + ",index0=" + getIndex0() + ",index1=" + getIndex1() + "]";
    }
}
