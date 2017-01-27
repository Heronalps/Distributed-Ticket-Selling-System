package com.ucsb.michaelzhang;

import java.io.*;
import java.net.ProxySelector;
import java.util.Scanner;
import static com.ucsb.michaelzhang.Config.*;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException{

        try {
            int totalNumOfDataCenter = Integer.parseInt(readConfig("Config","TotalNumOfDataCenter"));
            Process[] dataCenters = new Process[totalNumOfDataCenter];
            int tmpDT = totalNumOfDataCenter;
            int totalNumOfClient = Integer.parseInt(readConfig("Config","TotalNumOfClient"));
            Process[] clients = new Process[totalNumOfClient];
            int tmpCT = totalNumOfClient;

            String[] compile = {"bash", "-c", "find . -name \"*.java\" > source.txt; javac @source.txt; cp ConfigOriginal Config; rm Log_*"};

            ProcessBuilder compiler = new ProcessBuilder(compile);
            Process compilerProcess = compiler.start();
            compilerProcess.waitFor();


            while(totalNumOfDataCenter != 0) {
                int processID = tmpDT - totalNumOfDataCenter;
                System.out.println("Creating Process for Data Center D" + (processID) + " ...");
                String[] command = {"bash", "-c", "java -cp ./src com.ucsb.michaelzhang.DataCenter"};

                ProcessBuilder processBuilder = new ProcessBuilder(command);
                dataCenters[processID] = processBuilder.start();
                Thread.sleep(1000);

                PrintMessage printMessage = new PrintMessage(dataCenters[processID]);
                printMessage.start();
                Thread.sleep(1000);

                System.out.println("Data Center D" + (processID) + " is running ...");
                Thread.sleep(1000);
                totalNumOfDataCenter--;
            }

            while(totalNumOfClient != 0) {
                int clientID = tmpCT - totalNumOfClient;
                System.out.println("Creating Process for Client C" + (clientID) + " ...");
                String[] command = {"bash", "-c", "java -cp ./src com.ucsb.michaelzhang.Client"};

                ProcessBuilder processBuilder = new ProcessBuilder(command);
                clients[clientID] = processBuilder.start();
                Thread.sleep(1000);

                PrintMessage printMessage = new PrintMessage(clients[clientID]);
                printMessage.start();
                Thread.sleep(1000);

                System.out.println("Client C" + (clientID) + " is running ...");
                Thread.sleep(1000);
                totalNumOfClient--;

            }

            Thread.sleep(60 * 1000);

            System.out.println("Mission Complete! Closing Main Process ...");

            System.exit(0);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
