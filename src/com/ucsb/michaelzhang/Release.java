package com.ucsb.michaelzhang;

import java.io.Serializable;

/**
 * Created by michaelzhang on 1/21/17.
 */
public class Release extends Message implements Serializable{
    String type;
    int numOfTicketDecreased;

    public String getType() {
        return this.type;
    }

    public Release(int numOfTicket) {
        numOfTicketDecreased = numOfTicket;
        type = "RELEASE";
    }
}
