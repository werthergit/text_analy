package com.adt.text.analy.text_analy;

public class TestHeap {

    static String[] a = new String[102];

    static void t1(){
        a = new String[1024];
        t1();
    }

    public static void main(String[] args) {

        t1();
    }
}
