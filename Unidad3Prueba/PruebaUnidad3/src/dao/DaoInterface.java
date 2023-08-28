package dao;

import java.io.IOException;

public interface DaoInterface<T>{

    public void save(T obj) throws IOException;
    public void update(T obj, Integer pos) throws IOException;
    public list.MyLinkedList<T> getAll();

}