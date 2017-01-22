package com.ucsb.michaelzhang;

import java.util.Comparator;

/**
 * Created by michaelzhang on 1/21/17.
 */
public class Request extends Message implements Comparator<Request>{
    String type = "REQUEST";
    int numOfTicket;
    TimeStamp timeStamp;

    public Request(int numOfTicketRequested, TimeStamp timeStamp){
        numOfTicket = numOfTicketRequested;
        this.timeStamp = timeStamp;
    }

    @Override
    public int compare(Request rq1, Request rq2) {
        return rq1.timeStamp.compareTo(rq2.timeStamp);
    }
}
