package com.ucsb.michaelzhang;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import static com.ucsb.michaelzhang.Config.*;

/**
 * Created by michaelzhang on 1/20/17.
 */
public class Client {
    String clientID;

    public Client(int id){
        clientID = "C" + id;
    }

    // Create a socket and send ClientRequest to corresponding DataCenter
    public void createSocketClient(int numOfTicket) throws IOException{
        try {
            //Find the corresponding DataCenter's port

            int port = Integer.parseInt(readConfig("Config","DataCenterD" + this.clientID.substring(1,2) + "_PORT"));
            String hostname = readConfig("Config", "Hostname");
            Socket clientSocket = new Socket(hostname, port);
            ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());

            ClientRequest clientRequest = new ClientRequest(numOfTicket);
            //Send to Socket server
            outToServer.writeObject(clientRequest);

        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void buy(int numOfTickets) throws IOException{
        createSocketClient(numOfTickets);
        receiveReply();
        displayResult();
    }

    public void receiveReply() throws IOException {

    }



    public void displayResult() {

    }

}
