package com.ucsb.michaelzhang;

import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Properties;

/**
 * Created by michaelzhang on 1/20/17.
 */
public class DataCenter {

    String dataCenterID;
    int globalTicketNumber;

    // Lamport Clock. Basically it is a counter and the first component in TimeStamp <LamportClock, DataCenterID>
    int lamportClock;

    //Specific comparator has been defined in Class Request and TimeStamp
    PriorityQueue<Request> RequestHeap;

    //Every DataCenter needs to know other datacenter's existence
    ArrayList<String> existingDataCenter;


    private DataCenter(String dataCenterID, int globalTicketNumber, int lamportClock) {
        //New dataCenterID, Read GlobalTicketNumber, LamportClock, Initiate new RequestHeap

        this.dataCenterID = dataCenterID;
        this.globalTicketNumber = globalTicketNumber;
        this.lamportClock = lamportClock;

        RequestHeap = new PriorityQueue<Request>();
    }

    public static void main(String[] args) throws IOException {
        String nextDataCenterID = readConfig("DataCenter_Config", "NextDataCenterID");
        String globalTicketNumber = readConfig("DataCenter_Config", "GlobalTicketNumber");
        String lamportClock = readConfig("DataCenter_Config", "LamportClock");

        createDataCenter(nextDataCenterID, Integer.valueOf(globalTicketNumber), Integer.valueOf(lamportClock));
        String next = "D" + (Integer.valueOf(nextDataCenterID.substring(1,2)) + 1);
        changeProperty("DataCenter_Config","NextDataCenterID", next);
    }

    public static String readConfig(String filename, String key) throws IOException{
        String value = null;
        try(FileReader reader = new FileReader(filename)){
            Properties properties = new Properties();
            properties.load(reader);
            value = properties.getProperty(key);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return value;
    }

    //Change the specific property while preserving the previous properties

    public static void changeProperty(String filename, String key, String value) throws IOException {

        final File tmpFile = new File(filename + ".tmp");
        final File file = new File(filename);

        //New printWriter will empty the file. That's why it needs a tmp.
        PrintWriter pw = new PrintWriter(tmpFile);
        BufferedReader br = new BufferedReader(new FileReader(file));
        boolean found = false;
        final String toAdd = key + '=' + value;

        for (String line; (line = br.readLine()) != null; ) {
            if (line.startsWith(key + '=')) {
                line = toAdd;
                found = true;
            }
            pw.println(line);
        }
        if (!found) {
            pw.println(toAdd);
        }
        br.close();
        pw.close();

        tmpFile.renameTo(file);
    }

    public static void createDataCenter(String dataCenterID, int globalTicketNumber, int lamportClock) {

        System.out.println("Creating New DataCenter...");

        DataCenter dc = new DataCenter(dataCenterID, globalTicketNumber, lamportClock);



    }

    public int requestGlobalTicketNumber(){
        return 0;
    }

    public void sendRequest(String dataCenterID, int numOfTicketRequested){

    }

    public void sendReply(String dataCenterID){

    }

    public void sendRelease(String dataCenterID, int numOfTicketDecreased){

    }

    public boolean sellTicket(int numOfTicket){
        return true;
    }

    public void replyClient(){

    }

}
