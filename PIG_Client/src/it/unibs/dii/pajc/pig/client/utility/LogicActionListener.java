package it.unibs.dii.pajc.pig.client.utility;

import java.util.EventListener;

public interface LogicActionListener<T> extends EventListener {

    boolean performInsertAction(LogicActionEvent<T> evt);
    boolean performChangeAction(LogicActionEvent<T> evt);
    boolean performRemoveAction(LogicActionEvent<T> evt);

}
