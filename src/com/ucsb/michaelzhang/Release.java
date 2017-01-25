package com.ucsb.michaelzhang;

import java.io.Serializable;

/**
 * Created by michaelzhang on 1/21/17.
 */
public class Release extends Message implements Serializable{
    String type;
    int numOfTicketDecreased;
    String dataCenterID;

    public String getType() {
        return this.type;
    }

    public Release(int numOfTicket, String dataCenterID) {
        numOfTicketDecreased = numOfTicket;
        type = "RELEASE";
        this.dataCenterID = dataCenterID;
    }
}
