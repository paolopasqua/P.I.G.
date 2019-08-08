package it.unibs.dii.pajc.pig.client.utility;

import java.awt.*;

public class LogicActionEvent<T> extends AWTEvent {

    private long when;
    private T data;

    public LogicActionEvent(Object source, int id) {
        this(source, id, 0);
    }

    public LogicActionEvent(Object source, int id, long when) {
        this(source, id, when, null);
    }

    public LogicActionEvent(Object source, int id, long when, T data) {
        super(source, id);

        this.when = when;
        this.data = data;
    }

    public long getWhen() {
        return when;
    }

    public T getData() {
        return data;
    }
}
