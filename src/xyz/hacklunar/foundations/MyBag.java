package xyz.hacklunar.foundations;

import java.util.Iterator;

public class MyBag<Item> implements Iterable<Item> {
    public MyBag() {
    }
    void add(Item Item){

    }
    boolean isEmpty(){
        return true;
    }

    int size(){
        return 1;
    }

    @Override
    public Iterator<Item> iterator() {
        return null;
    }
}
