package com.android.zera.teamproject_app;

import java.util.ArrayList;

/**
 * Containerklasse für die Datenkapselung der Nachrichtenparameter.
 * @author Fabian Theuerkauf
 * @version 1.0
 */

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

    /**
     * Liefert den Sender-Parameter der Nachricht.
     * @return sender-Parameter
     */
    public String getSender() {
        return sender;
    }

    /**
     * Setzt den Sender der Nachricht.
     * @param sender Sender der Nachricht
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Liefert die ID-Parameter der Nachricht.
     * @return id-Parameter
     */
    public int getId() {
        return id;
    }

    /**
     * Setzt den ID-Parameter der Nachricht.
     * @param id eindeutige Nummer der Nachricht
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Liefert den Code-Parameter der Nachricht.
     * @return code-Parameter
     */
    public String getCode() {
        return code;
    }

    /**
     * Setzt den Code-Parameter der Nachricht.
     * @param code Nachrichtencode
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Liefert Auswahl der Favoriten-Parameter der Nachricht.
     * @return Auswahl der Favoriten-Parameter
     */
    public ArrayList<Integer> getSelection() {
        return selection;
    }

    /**
     * Setzt den Auswahl der Favoriten-Parameter der Nachricht.
     * @param selection Auswahl aller relevanten Buslinien
     */
    public void setSelection(ArrayList<Integer> selection) {
        this.selection = selection;
    }

    /**
     * Liefert die BushaltenstellenID-Parameter der Nachricht.
     * @return BushaltestellenID-Parameter
     */
    public int getStopID() {
        return stopID;
    }

    /**
     * Setzt den BushaltestellenID-Parameter der Nachricht.
     * @param stopID eindeutige BushaltestellenID
     */
    public void setStopID(int stopID) {
        this.stopID = stopID;
    }

    /**
     * Liefert den Breitengrad-Parameter der Nachricht.
     * @return Breitengrad-Parameter
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Setzt den Breitengrad-Parameter der Nachricht.
     * @param latitude Breitengrad der Bushaltestelle
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Liefert den Längengrad-Parameter der Nachricht.
     * @return Längengrad-Parameter
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Setzt den Längengrad-Parameter der Nachricht.
     * @param longitude Längengrad der Bushaltestelle
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Liefert den Zeit-Parameter der Nachricht.
     * @return time-Parameter
     */
    public int getTime() {
        return time;
    }

    /**
     * Setzt den Time-Parameter der Nachricht.
     * @param time aktuelle Zeit
     */
    public void setTime(int time) {
        this.time = time;
    }
}
