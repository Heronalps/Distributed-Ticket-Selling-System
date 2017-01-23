package com.ucsb.michaelzhang;

import java.util.Comparator;

/**
 * Created by michaelzhang on 1/21/17.
 */
public class DataCenterRequest extends Message implements Comparator<DataCenterRequest>{
    String type = "DATACENTERREQUEST";
    int numOfTicket;
    TimeStamp timeStamp;

    public DataCenterRequest(int numOfTicketRequested, TimeStamp timeStamp){
        numOfTicket = numOfTicketRequested;
        this.timeStamp = timeStamp;
    }

    @Override
    public int compare(DataCenterRequest rq1, DataCenterRequest rq2) {
        return rq1.timeStamp.compareTo(rq2.timeStamp);
    }
}
