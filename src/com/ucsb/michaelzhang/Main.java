package com.ucsb.michaelzhang;

import java.io.IOException;
import java.net.ProxySelector;
import java.util.Scanner;
import static com.ucsb.michaelzhang.Config.*;

public class Main {

    /*public static String getWorkingDirectory() throws IOException {
        BufferedReader pwd = null;
        try {
            Process readPWD = Runtime.getRuntime().exec("pwd");
            pwd = new BufferedReader(new
                    InputStreamReader(readPWD.getInputStream()));
            String workingDirectory = pwd.readLine();
            return workingDirectory;
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        finally {
            pwd.close();
        }
        return null;
    }
    */

    public static void outputProcess(Process process){

        Scanner scan = new Scanner(process.getInputStream());

        while (scan.hasNext()) {
            System.out.println(scan.next());
        }
        scan.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException{

        try {
            int totalNumOfDataCenter = Integer.parseInt(readConfig("Config","TotalNumOfDataCenter"));
            Process[] dataCenters = new Process[totalNumOfDataCenter];
            int tmpDT = totalNumOfDataCenter;
            int totalNumOfClient = Integer.parseInt(readConfig("Config","TotalNumOfClient"));
            Process[] clients = new Process[totalNumOfClient];
            int tmpCT = totalNumOfClient;

            while(totalNumOfDataCenter != 0) {
                System.out.println("Creating Process for Data Center D" + (tmpDT - totalNumOfDataCenter) + " ...");
                String[] command = {"bash", "-c", "java -cp ./src com.ucsb.michaelzhang.DataCenter"};

                ProcessBuilder processBuilder = new ProcessBuilder(command);
                //processBuilder.directory(new File(workingDirectory));

                dataCenters[tmpDT - totalNumOfDataCenter] = processBuilder.start();
                System.out.println("Data Center D" + (tmpDT - totalNumOfDataCenter) + " is running ...");

                totalNumOfDataCenter--;
            }

            while(totalNumOfClient != 0) {
                System.out.println("Creating Process for Client C" + (tmpCT - totalNumOfClient) + " ...");
                String[] command = {"bash", "-c", "java -cp ./src com.ucsb.michaelzhang.Client"};

                ProcessBuilder processBuilder = new ProcessBuilder(command);

                clients[tmpCT - totalNumOfClient] = processBuilder.start();
                System.out.println("Client C" + (tmpCT - totalNumOfClient) + " is running ...");

                totalNumOfClient--;
            }

            // Scan every process in the array, and output its feedback


        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
