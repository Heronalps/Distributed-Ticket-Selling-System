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

    String dataCenterID; //D0, D1, D2 ....
    int globalTicketNumber;
    String hostname;
    int port;
    int requestTicketNum;
    int totalNumOfDataCenter;
    String logFile;



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
        this.totalNumOfDataCenter = Integer.parseInt(readConfig("Config", "TotalNumOfDataCenter"));
        this.logFile = "Log_" + dataCenterID;

        // Update NextDataCenterID and add Datacenter Port
        String next = "D" + (Integer.valueOf(this.dataCenterID.substring(1,2)) + 1);
        changeProperty("Config", "NextDataCenterID", next);
        changeProperty("Config", "DataCenter" + this.dataCenterID + "_PORT", String.valueOf(this.port));
        changeProperty("Config", "NextPort", String.valueOf(this.port + 1));


        dataCenterRequestHeap = new PriorityQueue<>(10, new DataCenterRequest());
        clientRequestQueue = new LinkedList<>();
        replyToDataCenterArray = new ArrayList<>();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        DataCenter dataCenter = new DataCenter();

        System.out.println("Creating New DataCenter " + dataCenter.dataCenterID + " ...");

        dataCenter.startSocketServer();

        // For now, one datacenter can only serve one client. Will implement multiple clients if time permits.
    }

    public void startSocketServer() throws IOException, ClassNotFoundException, InterruptedException{

        //logMessage(logFile);
        System.out.println(this.dataCenterID + ": Socket Server Established at port " + this.port + " and Listening to Messages ...");
        System.out.println(this.dataCenterID + ": There are " + globalTicketNumber + " tickets left in the pool ...");
        try(
                ServerSocket serverSocket = new ServerSocket(this.port);
                ){
            while (true) {
                try (
                        Socket clientSocket = serverSocket.accept();
                        ObjectInputStream inFromClient =
                                new ObjectInputStream(clientSocket.getInputStream())
                ) {
                    System.out.println(this.dataCenterID + ": A Message Received ...");

                    //Delay for 2 second on every message received
                    Thread.sleep(2000);
                    Message msg = (Message) inFromClient.readObject();


                    if (msg.getType().equals("CLIENTREQUEST")) {

                        ClientRequest clientRequest = (ClientRequest) msg;
                        requestTicketNum = clientRequest.numOfTicketRequest;
                        System.out.println(this.dataCenterID + ": A Client Request Received from "
                                + clientRequest.clientID + " to buy " + requestTicketNum + " tickets ... ");

                        int id = Integer.valueOf(dataCenterID.substring(1, 2));

                        TimeStamp ts = new TimeStamp(this.lamportClock, id);
                        DataCenterRequest dataCenterRequest =
                                new DataCenterRequest(requestTicketNum, ts, dataCenterID);
                        broadcastRequest(dataCenterRequest);
                        clientRequestQueue.add(clientRequest);
                        dataCenterRequestHeap.add(dataCenterRequest);
                        clockIncrement();


                    } else if (msg.getType().equals("DATACENTERREQUEST")) {

                        DataCenterRequest dataCenterRequest = (DataCenterRequest) msg;
                        System.out.println(this.dataCenterID + ": A Data Center Request Received from "
                                + dataCenterRequest.dataCenterID + " to buy " + dataCenterRequest.numOfTicket + " tickets ... ");
                        dataCenterRequestHeap.add(dataCenterRequest);
                        setLocalClock(dataCenterRequest.timeStamp.lamportClock);
                        clockIncrement();
                        sendReply(dataCenterRequest.dataCenterID);


                    } else if (msg.getType().equals("REPLYTODATACENTER")) {

                        ReplyToDataCenter replyToDataCenter = (ReplyToDataCenter) msg;
                        System.out.println(this.dataCenterID + ": A Reply to Data Center Received from " + replyToDataCenter.dataCenterID + " ... ");
                        replyToDataCenterArray.add(replyToDataCenter);
                        clockIncrement();

                        // If received all replies and the first request in the client request queue is also at top of the heap

                        if (replyToDataCenterArray.size() == (totalNumOfDataCenter - 1)
                                && (dataCenterRequestHeap.peek().dataCenterID == this.dataCenterID)) {

                            boolean success = this.sellTicket(requestTicketNum);
                            replyClient(success, this.dataCenterID);
                            // Clear Reply To Data Center array to avoid messing with following Replies
                            replyToDataCenterArray.clear();
                        }


                    } else if (msg.getType().equals("RELEASE")) {

                        Release release = (Release) msg;
                        System.out.println(this.dataCenterID + ": A Release Received from "
                                + release.dataCenterID + " with " + release.numOfTicketDecreased + " tickets sold ... ");

                        globalTicketNumber -= release.numOfTicketDecreased;
                        System.out.println(this.dataCenterID + ": " + globalTicketNumber + " tickets left in the pool ...");

                        if (!dataCenterRequestHeap.isEmpty()) {
                            dataCenterRequestHeap.poll();
                        }
                        clockIncrement();

                        // If received all replies and the first request in the client request queue is also at top of the heap

                        if (replyToDataCenterArray.size() == (totalNumOfDataCenter - 1)
                                && (dataCenterRequestHeap.peek().dataCenterID == this.dataCenterID)) {

                            boolean success = this.sellTicket(requestTicketNum);
                            replyClient(success, this.dataCenterID);
                            // Clear Reply To Data Center array to avoid messing with following Replies
                            replyToDataCenterArray.clear();
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }


    public synchronized void clockIncrement(){
        this.lamportClock++;
        System.out.println(this.dataCenterID + ": Lamport Clock increments to " + this.lamportClock + " ...");
    }

    public synchronized void setLocalClock(int messageTimeStamp){
        this.lamportClock = Math.max(this.lamportClock, messageTimeStamp) + 1;
    }

    //Broadcast request to all existing data centers
    public void broadcastRequest(DataCenterRequest dataCenterRequest) throws IOException{

        int totalNumOfDataCenter = Integer.parseInt(readConfig("Config","TotalNumOfDataCenter"));
        String host = readConfig("Config", "Hostname");

        for (int id = 0; totalNumOfDataCenter != 0; id++, totalNumOfDataCenter--){
            try {
                int port = Integer.parseInt(readConfig("Config", "DataCenterD" + id + "_PORT"));
                if (port != this.port) {
                    Socket socket = new Socket(host, port);
                    ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
                    outToServer.writeObject(dataCenterRequest);
                    System.out.println(this.dataCenterID + ": Broadcasting Data Center Request to Data Center at port " + port + " ...");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //Send Reply to data center which sent requests
    public void sendReply(String dataCenterID) throws IOException{
        ReplyToDataCenter reply = new ReplyToDataCenter(this.dataCenterID);
        int port = Integer.parseInt(readConfig("Config", "DataCenter" + dataCenterID + "_PORT"));
        String host = readConfig("Config", "Hostname");
        Socket socket = new Socket(host, port);
        ObjectOutputStream outToDataCenter = new ObjectOutputStream(socket.getOutputStream());
        try {
            outToDataCenter.writeObject(reply);
            System.out.println(this.dataCenterID + ": Sent Reply to Data Center " + dataCenterID + " at port " + port + "...");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            outToDataCenter.close();
            socket.close();
        }
    }


    //Access to Critical Section, which is the NumOfTicket in Config file
    public boolean sellTicket(int numOfTicket) throws IOException {

        int currentNum = Integer.parseInt(readConfig("Config", "GlobalTicketNumber"));
        if (currentNum >= numOfTicket) {
            changeProperty("Config", "GlobalTicketNumber", String.valueOf(currentNum - numOfTicket));
            System.out.println(this.dataCenterID + ": " + numOfTicket + " tickets have been sold ...");
            this.globalTicketNumber -= numOfTicket;
            broadcastRelease(numOfTicket);
            return true;
        } else {
            //Once calling sellTicket, even failed to decrease the num of ticket, the data center should broadcast the Release anyway.
            //If failed, broadcast release with 0 tickets.
            System.out.println(this.dataCenterID + ": Not sufficient tickets have been left ...");
            broadcastRelease(0);
            return false;
        }
    }

    public void broadcastRelease(int numOfTicketDecreased) throws IOException{

        Release release = new Release(numOfTicketDecreased, this.dataCenterID);
        int totalNumOfDataCenter = Integer.parseInt(readConfig("Config","TotalNumOfDataCenter"));
        String host = readConfig("Config", "Hostname");
        for (int id = 0; totalNumOfDataCenter != 0; id++, totalNumOfDataCenter--){
            int port = Integer.parseInt(readConfig("Config", "DataCenterD" + id + "_PORT"));
            if (port != this.port) {
                try (
                        Socket socket = new Socket(host, port);
                        ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
                ) {
                    outToServer.writeObject(release);
                    System.out.println(this.dataCenterID + ": Broadcasting Releases to Data Center D" + id + " ...");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void replyClient(boolean success, String dataCenterID) throws IOException{
        ReplyToClient reply = new ReplyToClient(success);
        //Send reply to corresponding client. For now, I assume the ID of client and datacenter are same. May be modified later.
        int port = Integer.parseInt(readConfig("Config", "ClientC" + dataCenterID.substring(1, 2) + "_PORT"));
        String hostname = readConfig("Config", "Hostname");

        try (
                Socket socket = new Socket(hostname, port);
             ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());

        ){
            outToClient.writeObject(reply);
            outToClient.flush();
            System.out.println(this.dataCenterID + ": Replying to Client C" + dataCenterID.substring(1, 2) + " ...");
        } catch (IOException ex){
            ex.printStackTrace();
        }

    }

    public synchronized static void logMessage(String logFile) throws IOException{
        File log = new File(logFile);
        if (!log.exists()) {
            log.createNewFile();
        }

        OutputStream outputStream = new FileOutputStream(log);
        PrintStream printStream = new PrintStream(outputStream);

        System.setOut(printStream);
    }
}

