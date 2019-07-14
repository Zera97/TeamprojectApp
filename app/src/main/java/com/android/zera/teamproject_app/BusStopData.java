package com.android.zera.teamproject_app;

/**
 *
 * Containerklasse für die Datenkapselung aller Parameter zur Beschreibung einer Bushaltestelle.
 * @author Fabian Theuerkauf
 * @version 1.0
 */
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

    /**
     * Liefert die Stringdarstellung des Objektes der Klasse BusStopData.
     * @return String bestehend aus allen Attributen.
     */
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
