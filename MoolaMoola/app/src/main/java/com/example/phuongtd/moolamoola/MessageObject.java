package com.example.phuongtd.moolamoola;

/**
 * Created by qs109 on 6/6/2016.
 */
public class MessageObject {
    Type type;
    String message;
    int value;

    public MessageObject(Type type, String message, int value) {
        this.type = type;
        this.message = message;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

