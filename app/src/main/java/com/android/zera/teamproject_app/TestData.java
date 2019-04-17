package com.android.zera.teamproject_app;

import java.util.ArrayList;

public class TestData {

    String sender;
    int id;
    String code;
    ArrayList<Integer> busLinien;

    public TestData(String sentBy, int id, String code,ArrayList<Integer> buslinien){
        this.sender = sentBy;
        this.id = id;
        this.code = code;
        this.busLinien = buslinien;
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
