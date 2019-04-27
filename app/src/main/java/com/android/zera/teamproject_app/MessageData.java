package com.android.zera.teamproject_app;

import java.util.ArrayList;

public class MessageData {

    private String sender;
    private int id;
    private String code;
    private ArrayList<Integer> selection;
    private int stopID;
    private int time;
    private double latitude;
    private double longitude;

    public MessageData(String sentBy, int id, String code){
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

    public ArrayList<Integer> getSelection() {
        return selection;
    }

    public void setSelection(ArrayList<Integer> selection) {
        this.selection = selection;
    }

    public int getStopID() {
        return stopID;
    }

    public void setStopID(int stopID) {
        this.stopID = stopID;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
