package com.ucsb.michaelzhang;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by michaelzhang on 1/21/17.
 */
public class TimeStamp implements Comparator<TimeStamp>, Comparable<TimeStamp>, Serializable{
    int lamportClock;
    int processID;

    //TimeStamp <1, D1> will be like <1, 1> for convenience. The second 1 represents Data Center 1.

    public TimeStamp(int clock, int dataCenterID) {
        lamportClock = clock;
        processID = dataCenterID;
    }

    @Override
    //In order to apply compareTo is class DataCenterRequest.

    public int compareTo(TimeStamp ts){
        if (this.lamportClock == ts.lamportClock) {
            return this.processID - ts.processID;
        } else {
            return this.lamportClock - ts.lamportClock;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TimeStamp)) {
            return false;
        } else {
            TimeStamp ts = (TimeStamp) obj;
            return compare(this, ts) == 0;
        }
    }

    @Override
    //Return negative, zero, positive when THIS is less than, equal, greater than ts.

    public int compare(TimeStamp ts1, TimeStamp ts2) {
        if (ts1.lamportClock == ts2.lamportClock) {
            return ts1.processID - ts2.processID;
        } else {
            return ts1.lamportClock - ts2.lamportClock;
        }
    }
}
