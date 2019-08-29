package it.unibs.dii.pajc.pig.client.utility;

import java.awt.*;

public class LogicActionEvent<T> extends AWTEvent {

    public static final int LOGIC_ACTION_FIRST = RESERVED_ID_MAX + 42;
    public static final int INSERT_LOGIC_ACTION = LOGIC_ACTION_FIRST;
    public static final int CHANGE_LOGIC_ACTION = LOGIC_ACTION_FIRST+1;
    public static final int REMOVE_LOGIC_ACTION = LOGIC_ACTION_FIRST+2;

    private long when;
    private T[] data;

    public LogicActionEvent(Object source, int id) {
        this(source, id, 0);
    }

    public LogicActionEvent(Object source, int id, long when) {
        this(source, id, when, null);
    }

    public LogicActionEvent(Object source, int id, long when, T[] data) {
        super(source, id);

        this.when = when;
        this.data = data;
    }

    public long getWhen() {
        return when;
    }

    public T[] getData() {
        return data;
    }
}
