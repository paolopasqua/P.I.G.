package it.unibs.dii.pajc.pig.client.utility;

import java.util.EventListener;

public interface LogicActionListener<T> extends EventListener {

    void performInsertAction(LogicActionEvent<T> evt);
    void performChangeAction(LogicActionEvent<T> evt);
    void performRemoveAction(LogicActionEvent<T> evt);

}
