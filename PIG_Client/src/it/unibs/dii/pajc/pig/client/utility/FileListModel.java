package it.unibs.dii.pajc.pig.client.utility;

import javax.swing.*;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileListModel<T extends Serializable> extends AbstractListModel<T> {
    private File file;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private ArrayList<T> cache;


    private FileListModel() {
        cache = new ArrayList<>();
    }

    public FileListModel(File file) {
        this();

        if (checkFile(file)) //Throws
            this.file = new File(file.getPath());
    }

    public FileListModel(String path) {
        this(new File(path));
    }

    private boolean checkFile(File file) {
        //TODO: check file
        return false;
    }

    public void load() {
        //TODO: load array from file
    }

    public void store() {
        //TODO: store array to file
    }

    @Override
    public int getSize() {
        return cache.size();
    }

    @Override
    public T getElementAt(int i) {
        return cache.get(i);
    }

    public int getIndex(T elem) {
        return cache.indexOf(elem);
    }

    public List<T> getElements() {
        return (ArrayList<T>) cache.clone();
    }

    public void addElement(T elem) {
        cache.add(elem);
        fireIntervalAdded(this, cache.size()-1, cache.size()-1);
    }

    public void removeElementAt(int index) {
        cache.remove(index);
        fireIntervalRemoved(this, index, index);
    }

    public void removeInterval(int index0, int index1) {

    }

    public void updateElementAt(T elem, int index) {
        cache.set(index, elem);
        fireContentsChanged(this, index, index);
    }
}
