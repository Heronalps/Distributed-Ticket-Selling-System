package com.ucsb.michaelzhang;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by michaelzhang on 1/24/17.
 */
public class TestServer {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(1888);
        while(true) {
            try (
                    Socket socket = serverSocket.accept();
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ) {
                Object msg = ois.readObject();

                if (msg instanceof ClientRequest) {
                    System.out.println(((ClientRequest) msg).numOfTicketRequest);

                    ReplyToClient reply = new ReplyToClient(true);
                    oos.writeObject(reply);
                } else if (msg instanceof DataCenterRequest) {
                    System.out.println("DataCenterRequest");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
