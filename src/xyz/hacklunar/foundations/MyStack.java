package xyz.hacklunar.foundations;

import java.util.Iterator;

public class MyStack<Item> implements Iterable<Item> {
    private int N;
    private Item[] data;
    public MyStack() {
        N = 0;
        data = (Item[]) new Object[10];
    }

    public static void main(String[] args) {
        MyStack<String> list = new MyStack<>();
        list.push("Hackeem");
        list.push("Mensah");
        list.push("Bosu");
        System.out.println(list.size());
        System.out.println(list.pop());
        System.out.println(list.size());
    }

    public void push(Item item){
        if (N == data.length) resize(data.length*2);
        data[N++] = item;
    }

    private void resize(int newCapacity) {
        Item[] temp = (Item[]) new Object[newCapacity];
        for (int i = 0; i < N; i++) {
            temp[i] = data[i];
        }
        data = temp;
    }

    public Item pop(){
        Item item = data[--N];
        data[N] = null;
        if ( N > 0 && N == data.length/4) resize(data.length / 2);
        return item;
    }

    public boolean isEmpty(){
        return N == 0;
    }

    public int size(){
        return N;
    }
    @Override
    public Iterator<Item> iterator() {
        return new StackIterator();
    }

    class StackIterator implements Iterator<Item> {
        private int i = N;
        @Override
        public boolean hasNext() {
            return i > 0;
        }

        @Override
        public Item next() {
            return data[--i];
        }
    }
}
