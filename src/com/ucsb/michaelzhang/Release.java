package com.ucsb.michaelzhang;

/**
 * Created by michaelzhang on 1/21/17.
 */
public class Release extends Message {
    String type = "RELEASE";
    int numOfTicketDecreased;

    public Release(int numOfTicket) {
        numOfTicketDecreased = numOfTicket;
    }
}
