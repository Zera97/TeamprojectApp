package com.android.zera.teamproject_app;

public class TestData {

    String sentBy;
    int id;
    String code;

    public TestData(String sentBy, int id, String code){
        this.sentBy = sentBy;
        this.id = id;
        this.code = code;
    }

    public String getSender() {
        return sentBy;
    }

    public void setSender(String sender) {
        this.sentBy = sender;
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
