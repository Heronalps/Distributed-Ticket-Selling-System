package com.ucsb.michaelzhang;

import java.io.Serializable;

/**
 * Created by michaelzhang on 1/22/17.
 */
public class ClientRequest extends Message implements Serializable {
    String type;
    int numOfTicketRequest;
    String clientID;

    public String getType(){
        return this.type;
    }
    public ClientRequest(int numOfTicketRequest, String clientID){
        this.numOfTicketRequest = numOfTicketRequest;
        type = "CLIENTREQUEST";
        this.clientID = clientID;
    }
}
