package dao;

import list.MyLinkedList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DaoAdap<T> implements DaoInterface<T> {

    private Connection conex;
    private Class clas;
    private String url;

    public DaoAdap(Class clas) {
        this.conex = new Connection();
        this.clas = clas;
        this.url = Connection.URL + clas.getSimpleName().toLowerCase() + ".json";
    }

    @Override
    public void save(T obj) throws IOException {
        MyLinkedList<T> list = getAll();

        list.add(obj);
        
        conex.getxStream().alias(list.getClass().getName(), MyLinkedList.class);
        conex.getxStream().toXML(list, new FileWriter(url));
    }

    @Override
    public void update(T obj, Integer pos) throws IOException {

    }

    @Override
    public MyLinkedList<T> getAll() {
        MyLinkedList<T> list = new MyLinkedList<>();

        if (new File(this.url).exists()) {
            list = (MyLinkedList<T>) conex.
                    getxStream().
                    fromXML(new File(this.url));
        }

        return list;
    }

    public int generateId(){
        return getAll().size() + 1;
    }
}
