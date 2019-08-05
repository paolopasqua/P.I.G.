package it.unibs.dii.pajc.pig.client.utility;

import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MultiTypeModel {
    private HashMap<Class, ArrayList> data;
    protected EventListenerList listenerList = new EventListenerList();

    public MultiTypeModel() {
        data = new HashMap<>();
    }

    public void clear() {
        data.clear();
    }

    public <T extends Object> void clear(Class<T> type) {
        ArrayList<T> values = data.getOrDefault(type, null);

        if (values != null)
            values.clear();
    }

    public <T extends Object> void addElement(T element) {
        if (element == null)
            return;

        ArrayList<T> values = data.getOrDefault(element.getClass(), null);

        if (values == null) {
            values = new ArrayList<T>();
            data.put(element.getClass(), values);
        }

        values.add(element);
        int index = values.size()-1;
        fireIntervalAdded(this, element.getClass(), index, index);
    }

    public <T extends Object> T removeElementAt(Class<T> type, int index) {
        ArrayList<T> values = data.getOrDefault(type, null);

        if (values != null) {
            T old = values.remove(index);

            if (values.isEmpty()) {
                data.remove(type);
                fireContentsChanged(this, type,-1, -1);
            }
            else if (old != null)
                fireIntervalRemoved(this, type, index, index);

            return old;
        }

        return null;
    }

    public <T extends Object> void removeInterval(Class<T> type, int index0, int index1) {
        ArrayList<T> values = data.getOrDefault(type, null);

        if (values != null) {
            for (int i = index0; i <= index1; i++) {
                values.remove(i);
            }

            if (values.isEmpty()) {
                data.remove(type);
                fireContentsChanged(this, type,-1, -1);
            }
            else
                fireIntervalRemoved(this, type, index0, index1);
        }
    }

    public <T extends Object> boolean removeElement(T element) {
        if (element == null)
            return false;

        ArrayList<T> values = data.getOrDefault(element.getClass(), null);

        if (values != null && values.remove(element)) {
            int index = getElementIndex(element);

            return removeElementAt(element.getClass(), index) != null;
        }

        return false;
    }

    public <T extends Object> T replaceElementAt(int index, T element) {
        if (element == null)
            return null;

        ArrayList<T> values = data.getOrDefault(element.getClass(), null);

        if (values != null) {
            T old = values.set(index, element);
            fireContentsChanged(this, element.getClass(), index, index);
            return old;
        }

        return null;
    }

    public <T extends Object> List<T> getElements(Class<T> type) {
        ArrayList<T> values = data.getOrDefault(type, null);

        if (values != null)
            values = (ArrayList<T>)values.clone();

        return values;
    }

    public <T extends Object> T getElementAt(Class<T> type, int index) {
        ArrayList<T> values = data.getOrDefault(type, null);
        T element = null;

        if (values != null) {
            element = values.get(index);
        }

        return element;
    }

    public <T extends Object> boolean containsElement(T element) {
        if (element == null)
            return false;

        ArrayList<T> values = data.getOrDefault(element.getClass(), null);

        if (values != null)
            return values.contains(element);

        return false;
    }

    public <T extends Object> int getElementIndex(T element) {
        if (element == null)
            return -1;

        ArrayList<T> values = data.getOrDefault(element.getClass(), null);

        if (values != null)
            return values.indexOf(element);

        return -1;
    }

    public void addListDataListener(ListMultiTypeListener l) {
        this.listenerList.add(ListMultiTypeListener.class, l);
    }

    public void removeListDataListener(ListMultiTypeListener l) {
        this.listenerList.remove(ListMultiTypeListener.class, l);
    }

    public ListMultiTypeListener[] getListDataListeners() {
        return this.listenerList.getListeners(ListMultiTypeListener.class);
    }

    protected <T extends Object> void fireContentsChanged(Object source, Class<T> elementType, int index0, int index1) {
        Object[] listeners = this.listenerList.getListenerList();
        ListMultiTypeEvent e = null;

        for(int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListMultiTypeListener.class) {
                if (e == null) {
                    e = new ListMultiTypeEvent(source, ListDataEvent.CONTENTS_CHANGED, elementType, index0, index1);
                }

                ((ListMultiTypeListener)listeners[i + 1]).contentsChanged(e);
            }
        }

    }

    protected <T extends Object> void fireIntervalAdded(Object source, Class<T> elementType, int index0, int index1) {
        Object[] listeners = this.listenerList.getListenerList();
        ListMultiTypeEvent e = null;

        for(int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListMultiTypeListener.class) {
                if (e == null) {
                    e = new ListMultiTypeEvent(source, ListDataEvent.INTERVAL_ADDED, elementType, index0, index1);
                }

                ((ListMultiTypeListener)listeners[i + 1]).intervalAdded(e);
            }
        }

    }

    protected <T extends Object> void fireIntervalRemoved(Object source, Class<T> elementType, int index0, int index1) {
        Object[] listeners = this.listenerList.getListenerList();
        ListMultiTypeEvent e = null;

        for(int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListMultiTypeListener.class) {
                if (e == null) {
                    e = new ListMultiTypeEvent(source, ListDataEvent.INTERVAL_REMOVED, elementType, index0, index1);
                }

                ((ListMultiTypeListener)listeners[i + 1]).intervalRemoved(e);
            }
        }

    }
}
