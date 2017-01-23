package com.ucsb.michaelzhang;

/**
 * Created by michaelzhang on 1/20/17.
 */
public abstract class Message {
    String type; //DataCenterRequest, ClientRequest, ReplyToDataCenter, ReplyToDataCenter, Release
    TimeStamp timeStamp;
}