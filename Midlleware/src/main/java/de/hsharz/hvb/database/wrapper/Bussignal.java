package de.hsharz.hvb.database.wrapper;

import org.json.JSONObject;

public class Bussignal {

    private int bbID;
    private String busLine;
    private double longitude;
    private double latitude;
    private String signal;
    private String type;

    public Bussignal ( int nID, String nBusLine, double nLongitude, double nLatitude, String nSignal, String nType ) {
        this.bbID = nID;
        this.busLine = nBusLine;
        this.longitude = nLongitude;
        this.latitude = nLatitude;
        this.signal = nSignal;
        this.type = nType;
    }

    public Bussignal ( JSONObject json ) {
        this.bbID = json.getInt ( "boxId" );
        this.busLine = json.getString ( "line" );
        this.longitude = Double.valueOf ( json.getString ( "longitude" ) );
        this.latitude = Double.valueOf ( json.getString ( "latitude" ) );
        this.signal = json.getString ( "signal" );
        this.type = json.getString ( "nettype" );
    }

    public Bussignal ( ) {

    }

    public int getBBID ( ) { return this.bbID; }

    public String getBusLine ( ) { return this.busLine; }

    public double getLongitude ( ) { return this.longitude; }

    public double getLatitude ( ) { return this.latitude; }

    public String getSignal ( ) { return this.signal; }

    public String getType ( ) { return this.type; }

    public void setBBID ( int nID) { this.bbID = nID;}

    public void setBusLine ( String nBusline ) { this.busLine = nBusline; }

    public void setLongitude ( double nLongitude ) { this.longitude = nLongitude; }

    public void setLatitude ( double nLatitude ) { this.latitude = nLatitude; }

    public void setSignal ( String nSignal ) { this.signal = nSignal; }

    public void setType ( String nType ) { this.type = nType; }

    @Override
    public String toString ( ) {
        return "Blackbox Id: " + this.bbID
                + "; line: " + this.busLine
                + "; longitude: " + this.longitude
                + "; latitude: " + this.latitude
                + "; signal: " + this.signal
                + "; nettype" + this.type;
    }

    public JSONObject toJSONObject ( ) {

        JSONObject json = new JSONObject ( );
        json.put ( "bbid", this.bbID );
        json.put ( "line", this.busLine );
        json.put ( "latitude", this.latitude );
        json.put ( "longitude", this.longitude );
        json.put ( "signal", this.signal );
        json.put ( "nettype", this.type );
        return json;
    }
}
