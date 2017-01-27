package com.ucsb.michaelzhang;

import java.io.Serializable;

/**
 * Created by michaelzhang on 1/21/17.
 */
public class Release extends Message implements Serializable{
    String type;
    int numOfTicketDecreased;
    String dataCenterID;
    int lamportClock;

    public String getType() {
        return this.type;
    }

    public Release(int numOfTicket, String dataCenterID, int lamportClock) {
        numOfTicketDecreased = numOfTicket;
        type = "RELEASE";
        this.dataCenterID = dataCenterID;
        this.lamportClock = lamportClock;
    }
}
