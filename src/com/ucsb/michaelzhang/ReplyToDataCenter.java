package com.ucsb.michaelzhang;

import java.io.Serializable;

/**
 * Created by michaelzhang on 1/21/17.
 */
public class ReplyToDataCenter extends Message implements Serializable{
    String type;
    String dataCenterID; //The ID of Data Center sending the reply
    int lamportClock;

    public String getType() {
        return this.type;
    }

    public ReplyToDataCenter(String dataCenterID, int lamportClock) {
        this.dataCenterID = dataCenterID;
        type = "REPLYTODATACENTER";
        this.lamportClock = lamportClock;
    }
}
