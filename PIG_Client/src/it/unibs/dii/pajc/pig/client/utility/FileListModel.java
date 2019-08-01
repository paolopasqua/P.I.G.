package it.unibs.dii.pajc.pig.client.utility;

import javax.swing.*;
import java.io.*;
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

    public FileListModel(File file) throws IOException {
        this();

        if (file == null)
            throw new IllegalArgumentException("FileListModel(): No file specified (null value).");

        if (checkFile(file)) //Throws
            this.file = new File(file.getPath());
    }

    public FileListModel(String path) throws IOException {
        this(new File(path));
    }

    private boolean checkFile(File file) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        if(!file.canRead())
            if (!file.setReadable(true))
                throw new IOException("FileListModel.checkFile: Can't validate file if not readable.");

        if (!file.canWrite())
            if (!file.setWritable(true))
                throw new IOException("FileListModel.checkFile: Can't validate file if not writable.");

        return true;
    }

    public void load() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);

        if (fis.available() > 0) {
            reader = new ObjectInputStream(fis);
            Object read = null;

            try {
                while ((read = reader.readObject()) != null)
                    addElement((T) read);
            }
            catch (EOFException e) {
                //Nothing to do
            }

            reader.close();
        }

        fis.close();
    }

    public void store() throws IOException {
        writer = new ObjectOutputStream(new FileOutputStream(file));

        for (T t : cache) {
            writer.writeObject(t);
        }

        writer.close();
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

    public void updateElementAt(T elem, int index) {
        cache.set(index, elem);
        fireContentsChanged(this, index, index);
    }
}
