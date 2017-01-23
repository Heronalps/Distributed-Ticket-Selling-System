package com.ucsb.michaelzhang;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import static com.ucsb.michaelzhang.Config.*;


/**
 * Created by michaelzhang on 1/20/17.
 */
public class DataCenter {

    String dataCenterID;
    int globalTicketNumber;
    String hostname;
    int port;

    // Lamport Clock. Basically it is a counter and the first component in TimeStamp <LamportClock, DataCenterID>
    int lamportClock;

    //Specific comparator has been defined in Class DataCenterRequest and TimeStamp
    PriorityQueue<DataCenterRequest> dataCenterRequestHeap;
    Queue<ClientRequest> clientRequestQueue;
    ArrayList<ReplyToDataCenter> replyToDataCenterArray;

    //DataCenter knows other datacenter's existence through Config



    private DataCenter() throws IOException {
        //New dataCenterID, Read GlobalTicketNumber, LamportClock, Initiate new dataCenterRequestHeap

        this.dataCenterID = readConfig("Config", "NextDataCenterID");
        this.globalTicketNumber = Integer.parseInt(readConfig("Config", "GlobalTicketNumber"));
        this.lamportClock = Integer.parseInt(readConfig("Config", "LamportClock"));;
        this.hostname = readConfig("Config","Hostname");
        this.port = Integer.parseInt(readConfig("Config", "NextPort"));
        int totalNumOfDataCenter = Integer.parseInt(readConfig("Config", "TotalNumOfDataCenter"));

        // Update NextDataCenterID and add Datacenter Port
        String next = "D" + (Integer.valueOf(this.dataCenterID.substring(1,2)) + 1);
        changeProperty("Config", "NextDataCenterID", next);
        changeProperty("Config", "DataCenter" + this.dataCenterID + "_PORT", String.valueOf(this.port));
        changeProperty("Config", "NextPort", String.valueOf(++this.port));

        dataCenterRequestHeap = new PriorityQueue<>();
        clientRequestQueue = new LinkedList<>();
        replyToDataCenterArray = new ArrayList<>();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        System.out.println("Creating New DataCenter...");

        DataCenter dataCenter = new DataCenter();

        createSocketServer(dataCenter);

        //TODO Assign a socket and IP address to new data center
        // For now, one datacenter can only serve one client. Will implement multiple clients if time permits.

    }

    public static void createSocketServer (DataCenter dataCenter) throws IOException, ClassNotFoundException{
        ServerSocket serverSocket = new ServerSocket(dataCenter.port);

        while(true) {
            try (
                    Socket clientSocket = serverSocket.accept();
                    ObjectOutputStream outToClient =
                            new ObjectOutputStream(clientSocket.getOutputStream());
                    ObjectInputStream inFromClient =
                            new ObjectInputStream(clientSocket.getInputStream());
            ) {
                System.out.println("Socket Server Established...");
                Message msg = (Message) inFromClient.readObject();

                if (msg.type == "CLIENTREQUEST") {

                    ClientRequest clientRequest = (ClientRequest) msg;
                    int requestTicketNum = clientRequest.numOfTicketRequest;
                    int id = Integer.valueOf(dataCenter.dataCenterID.substring(1,2));

                    TimeStamp ts = new TimeStamp(dataCenter.lamportClock,id);
                    DataCenterRequest dataCenterRequest =
                            new DataCenterRequest(requestTicketNum, ts);

                    broadcastRequest(dataCenterRequest);
                    dataCenter.dataCenterRequestHeap.add(dataCenterRequest);
                    dataCenter.updateLamportClock();


                } else if (msg.type == "DATACENTERREQUEST") {
                    DataCenterRequest dataCenterRequest = (DataCenterRequest) msg;
                    dataCenter.dataCenterRequestHeap.add(dataCenterRequest);


                } else if (msg.type == "REPLYTODATACENTER") {
                    ReplyToDataCenter replyToDataCenter = (ReplyToDataCenter) msg;
                    dataCenter.replyToDataCenterArray.add(replyToDataCenter);



                } else if (msg.type == "RELEASE") {
                    Release release = (Release) msg;
                    dataCenter.globalTicketNumber -= release.numOfTicketDecreased;


                }
                //Parse the client request
                //Request ticket
                //Answer client

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    public void updateLamportClock(){
        this.lamportClock++;
    }

    // Update Global ticket number in Config
    public int updateGlobalTicketNumber(){
        return 0;
    }

    //Broadcast request to all existing data centers
    public static void broadcastRequest(DataCenterRequest dataCenterRequest){

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
