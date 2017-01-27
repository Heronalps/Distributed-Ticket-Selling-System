package com.ucsb.michaelzhang;

import java.io.*;
import java.net.*;
import java.net.Socket;
import static com.ucsb.michaelzhang.Config.*;

/**
 * Created by michaelzhang on 1/20/17.
 */
public class Client {
    String clientID; //C0, C1, C2 ...
    int numOfTicketRequested;
    String logFile;

    public Client() throws IOException{
        String id = readConfig("Config", "NextClientID");
        clientID = id;

        // Update NextDataCenterID and add Datacenter Port
        String next = "C" + (Integer.valueOf(this.clientID.substring(1)) + 1);
        changeProperty("Config", "NextClientID", next);

        numOfTicketRequested = Integer.parseInt(readConfig("Config", "Client" + clientID + "_TICKET"));
        logFile = "Log_" + clientID;
    }

    // Create a socket and send ClientRequest to corresponding DataCenter
    public void startSocketClient(int numOfTicket) throws IOException, ClassNotFoundException, InterruptedException{
        //Find the corresponding DataCenter's port
        System.out.println("Creating New Client " + clientID + " ...");
        logMessage(logFile);
        int port = Integer.parseInt(readConfig("Config","DataCenterD" + this.clientID.substring(1) + "_PORT"));
        String hostname = readConfig("Config", "Hostname");
        int serverPort = Integer.parseInt(readConfig("Config", "NextClientPort"));
        changeProperty("Config", "Client" + clientID + "_PORT", String.valueOf(serverPort));
        changeProperty("Config", "NextClientPort", String.valueOf(serverPort + 1));

        ServerSocket serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(serverPort));

        Socket clientSocket = new Socket(hostname, port);
        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());

        try
        {
            System.out.println(clientID + ": Socket Established at port " + port + " and trying to buy " + numOfTicket + " tickets ...");
            ClientRequest clientRequest = new ClientRequest(numOfTicket, clientID);
            //Send to Socket server
            outToServer.writeObject(clientRequest);
            Socket socket = serverSocket.accept();
            ObjectInputStream inFromDataCenter =
                    new ObjectInputStream(socket.getInputStream());
            try {
                //Receive ReplyToClient message from Data Center
                System.out.println(clientID + ": A Socket Server established and listening to Data Center's Reply ...");

                Thread.sleep(2000);
                Object reply = inFromDataCenter.readObject();
                displayResult((ReplyToClient) reply);

            } finally {
                inFromDataCenter.close();
                socket.close();
            }

        }
        catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            outToServer.close();
            clientSocket.close();
            serverSocket.close();
        }
    }

    public void buy(int numOfTickets) throws IOException, ClassNotFoundException, InterruptedException{
        startSocketClient(numOfTickets);
    }


    public void displayResult(ReplyToClient reply) {
        if (reply.success == true) {
            System.out.println(clientID + ": Successfully bought " + numOfTicketRequested + " tickets!");
        } else {
            System.out.println(clientID + ": Not enough tickets left!");
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

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{

       Client client = new Client();
       int numOfTicket = client.numOfTicketRequested;
       client.buy(numOfTicket);
        System.exit(0);
    }
}
