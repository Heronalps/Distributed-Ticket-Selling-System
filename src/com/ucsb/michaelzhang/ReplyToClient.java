package com.ucsb.michaelzhang;

import java.io.Serializable;

/**
 * Created by michaelzhang on 1/22/17.
 */
public class ReplyToClient extends Message implements Serializable {
    String type;
    boolean success;

    public String getType() {
        return this.type;
    }

    public ReplyToClient(boolean success) {
        this.success = success;
        type = "REPLYTOCLIENT";
    }
}
