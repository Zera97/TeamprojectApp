package com.android.zera.teamproject_app;

public class StopData {

    protected String name;
    protected String coordinate2; //lat
    protected String coordinate1; //longi

    public StopData(String name, String lat, String longi) {
        this.name = name;
        this.coordinate1 = lat;
        this.coordinate2 = longi;
    }

    @Override
    public String toString() {
        return "StopData{" +
                "Name='" + name + '\'' +
                ", longi='" + coordinate1 + '\'' +
                ", lat='" + coordinate2 + '\'' +
                '}';
    }

}
