package com.ucsb.michaelzhang;

import java.io.*;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created by michaelzhang on 1/24/17.
 */
public class Test {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Test! Test! Test!");
        Test2 test2 = new Test2();


        OutputStream outputStream = new FileOutputStream("Log_D1");
        PrintStream printStream = new PrintStream(outputStream);

        System.setOut(printStream);

        test2.print();

    }
}
