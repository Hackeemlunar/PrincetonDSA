package xyz.hacklunar.foundations;

import java.util.Iterator;
import java.util.stream.IntStream;

public class LinkedListStack<T> implements Iterable<T> {

    private int N;
    Node head;
    Node last;
    public static void main(String[] args) {
        LinkedListStack<Integer> st = new LinkedListStack<>();
        IntStream.range(1,6).forEach(st::add);
        System.out.println(st.size());
        st.print();
    }
    void print() {
        Node n = head;
        while (n != null) {
            System.out.println(n.data);
            n = n.Next;
        }
    }

    public void add(T data){
        Node node = new Node(data);
        if (head == null){
            head = node;
            last = head;
        }
        last.Next = node;
        last = last.Next;
        N++;
    }
    public int size() {return N;}
    @Override
    public Iterator<T> iterator() {
        return new LinkedListStackIterator();
    }

    class Node {
        public Node(T data) {
            this.data = data;
        }
        T data;
        Node Next;
    }

    class LinkedListStackIterator implements Iterator<T> {
        Node current = head;
        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            T data = current.data;
            current = current.Next;
            return data;
        }
    }
}