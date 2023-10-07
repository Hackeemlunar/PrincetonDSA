package xyz.hacklunar;

import edu.princeton.cs.algs4.StdDraw;
import xyz.hacklunar.foundations.LinkedListStack;

import java.awt.*;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        LinkedListStack<Integer> st = new LinkedListStack<>();
        IntStream.range(0,6).forEach(st::add);
        System.out.println(st.size());
    }
}