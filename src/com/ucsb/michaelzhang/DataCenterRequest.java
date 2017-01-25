package com.ucsb.michaelzhang;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by michaelzhang on 1/21/17.
 */
public class DataCenterRequest extends Message implements Comparator<DataCenterRequest>, Serializable{
    String type;

    //The Data Center that requests and will fulfill the ticket order. No the one that will be sent to.
    String dataCenterID;
    int numOfTicket;
    TimeStamp timeStamp;

    public String getType() {
        return this.type;
    }

    public DataCenterRequest(){

    }

    public DataCenterRequest(int numOfTicketRequested, TimeStamp timeStamp, String dataCenterID){
        numOfTicket = numOfTicketRequested;
        this.timeStamp = timeStamp;
        this.dataCenterID = dataCenterID;
        type = "DATACENTERREQUEST";
    }

    @Override
    // In Lamport Algorithm, timeStamp can't be exactly equal in any cases.
    public int compare(DataCenterRequest rq1, DataCenterRequest rq2) {
        return rq1.timeStamp.compareTo(rq2.timeStamp);
    }

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof DataCenterRequest)) {
            return false;
        } else {
            DataCenterRequest dsr = (DataCenterRequest) obj;
            return  (this.dataCenterID == dsr.dataCenterID)
                    && (this.numOfTicket == dsr.numOfTicket)
                    && (compare(this, dsr) == 0);
        }
    }
}
