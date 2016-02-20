package org.wings_lab.safedrive.parsers;

import java.util.Date;

public class M2XValueObject {
    public Date timestamp;
    public int value;

    public M2XValueObject(Date timestamp, int value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public M2XValueObject() {
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
