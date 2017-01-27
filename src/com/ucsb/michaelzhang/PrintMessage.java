package com.ucsb.michaelzhang;

import java.io.*;
import java.util.Scanner;

/**
 * Created by michaelzhang on 1/26/17.
 */
public class PrintMessage extends Thread{
    Process process;

    public PrintMessage(Process process){
        this.process = process;
    }

    @Override
     public void run() {
        Scanner scan = new Scanner(process.getInputStream()).useDelimiter("\\n");
        while(scan.hasNext()) {
            System.out.println(scan.next());
        }
        scan.close();
     }
}
