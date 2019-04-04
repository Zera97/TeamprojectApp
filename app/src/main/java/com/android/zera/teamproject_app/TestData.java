package com.android.zera.teamproject_app;

public class TestData {

    String sender;
    int id;
    String code;

    public TestData(String sentBy, int id, String code){
        this.sender = sentBy;
        this.id = id;
        this.code = code;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
