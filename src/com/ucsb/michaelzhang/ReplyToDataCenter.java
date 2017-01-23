package com.ucsb.michaelzhang;

/**
 * Created by michaelzhang on 1/21/17.
 */
public class ReplyToDataCenter extends Message {
    String type = "REPLYTODATACENTER";
    int dataCenterID;

    public ReplyToDataCenter(int proc_ID) {
        dataCenterID = proc_ID;
    }
}
