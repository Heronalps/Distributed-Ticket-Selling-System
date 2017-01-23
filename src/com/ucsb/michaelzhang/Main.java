package com.ucsb.michaelzhang;

import java.io.IOException;
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

    public static void main(String[] args) throws IOException, InterruptedException{

        try {
            int totalNumOfDataCenter = Integer.parseInt(readConfig("Config","TotalNumOfDataCenter"));

            while(totalNumOfDataCenter != 0) {

                String[] command = {"bash", "-c", "java -cp ./src com.ucsb.michaelzhang.DataCenter"};


                ProcessBuilder processBuilder = new ProcessBuilder(command);
                //processBuilder.directory(new File(workingDirectory));

                Process process = processBuilder.start();
                Scanner scan = new Scanner(process.getInputStream());

                while (scan.hasNext()) {
                    System.out.println(scan.next());
                }
                scan.close();

                totalNumOfDataCenter--;
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
