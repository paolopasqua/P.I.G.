package it.unibs.dii.pajc.pig.client.utility;

public abstract class LogicActionAdapter<T> implements LogicActionListener<T> {
    public LogicActionAdapter() {
    }

    @Override
    public boolean performInsertAction(LogicActionEvent<T> evt) {
        return false;
    }

    @Override
    public boolean performChangeAction(LogicActionEvent<T> evt) {
        return false;
    }

    @Override
    public boolean performRemoveAction(LogicActionEvent<T> evt) {
        return false;
    }
}
