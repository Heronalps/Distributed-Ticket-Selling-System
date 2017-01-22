package com.ucsb.michaelzhang;

/**
 * Created by michaelzhang on 1/21/17.
 */
public class Reply extends Message {
    String type = "REPLY";
    int dataCenterID;

    public Reply(int proc_ID) {
        dataCenterID = proc_ID;
    }
}
