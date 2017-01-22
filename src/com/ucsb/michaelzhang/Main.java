package com.ucsb.michaelzhang;

import java.io.IOException;
import java.util.Scanner;

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

    public static void main(String[] args) throws IOException{

        TimeStamp ts1 = new TimeStamp(1,0);
        TimeStamp ts2 = new TimeStamp(1,1);


        Request rq1 = new Request(3, ts1);
        Request rq2 = new Request(4, ts2);

        System.out.println(rq1.equals(rq2));

        try {
            String[] command = {"bash", "-c", "javac src/com/ucsb/michaelzhang/*.java; " +
                    "java -cp ./src com.ucsb.michaelzhang.DataCenter" };

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            //processBuilder.directory(new File(workingDirectory));

            Process process = processBuilder.start();

            Scanner scan = new Scanner(process.getInputStream());

            while (scan.hasNext()) {
                System.out.println(scan.next());
            }
            scan.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
