package xyz.hacklunar.foundations;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class SlowStack {
    private int N = 0;
    private String[] data;

    public SlowStack(int capacity) {
        data = new String[capacity];
    }
    public static void main(String[] args) {
        SlowStack stack = new SlowStack(100);
        while (!StdIn.isEmpty())
        {
            String item = StdIn.readString();
            if (!item.equals("-"))
                stack.push(item);
            else if (!stack.isEmpty()) StdOut.print(stack.pop() + " ");
        }
        StdOut.println("(" + stack.size() + " left on stack)");
    }

    public void push(String item){
        data[N++] = item;
    }

    public String pop(){
        return data[--N];
    }

    public boolean isEmpty(){
        return N == 0;
    }

    public int size(){
        return N;
    }

}
