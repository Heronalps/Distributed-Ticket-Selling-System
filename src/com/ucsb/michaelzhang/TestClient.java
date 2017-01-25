package com.ucsb.michaelzhang;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by michaelzhang on 1/24/17.
 */
public class TestClient {

    public static void main(String[] args) throws IOException, ClassNotFoundException{
        Socket socket = new Socket("localhost", 1888);
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        TimeStamp ts = new TimeStamp(1, 1);
        oos.writeObject(new DataCenterRequest(4, ts, "D0"));
        Socket socket2 = new Socket("localhost", 1888);
        ObjectInputStream ois2 = new ObjectInputStream(socket2.getInputStream());
        ObjectOutputStream oos2 = new ObjectOutputStream(socket2.getOutputStream());
        oos2.writeObject(new ClientRequest(4, "C0"));
        Object tmp = ois2.readObject();
        System.out.println(((ReplyToClient) tmp).success);
    }
}
