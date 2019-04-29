package com.android.zera.teamproject_app;

public class BusStopData {

    protected int id;
    protected String name;
    protected String latitude;
    protected String longitude;

    public BusStopData(String name, String latitude, String longitude,int id) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "BusStopData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
