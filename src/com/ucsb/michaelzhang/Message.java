package com.ucsb.michaelzhang;

import java.io.FileWriter;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by michaelzhang on 1/20/17.
 */
public abstract class Message implements Serializable {
    String type; //DataCenterRequest, ClientRequest, ReplyToDataCenter, ReplyToDataCenter, Release

    abstract public String getType();

}