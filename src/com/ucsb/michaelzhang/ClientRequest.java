package com.ucsb.michaelzhang;

/**
 * Created by michaelzhang on 1/22/17.
 */
public class ClientRequest extends Message {
    String type = "CLIENTREQUEST";
    int numOfTicketRequest;

    public ClientRequest(int numOfTicketRequest){
        this.numOfTicketRequest = numOfTicketRequest;
    }
}
