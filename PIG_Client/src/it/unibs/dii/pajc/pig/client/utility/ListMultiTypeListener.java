package it.unibs.dii.pajc.pig.client.utility;

import java.util.EventListener;

public interface ListMultiTypeListener extends EventListener {
    void intervalAdded(ListMultiTypeEvent var1);

    void intervalRemoved(ListMultiTypeEvent var1);

    void contentsChanged(ListMultiTypeEvent var1);
}
