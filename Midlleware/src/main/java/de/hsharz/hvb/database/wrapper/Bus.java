package de.hsharz.hvb.database.wrapper;

import org.json.JSONArray;
import org.json.JSONObject;

public class Bus {

    private int id;
    private int line;
    private String nextStop = null;
    private String previousStop = null;
    private double longitude;
    private double latitude;

    public Bus ( int nId, int nLine, String nNext, String nPrevious, double nLongitude, double nLatitude ) {
        this.id = nId;
        this.line = nLine;
        this.nextStop = nNext;
        this.previousStop = nPrevious;
        this.longitude = nLongitude;
        this.latitude = nLatitude;
    }

    public Bus ( ) {
    }

    public int getId ( ) {
        return this.id;
    }

    public int getLine ( ) {
        return this.line;
    }

    public String getNextStop ( ) {
        return this.nextStop;
    }

    public String getPreviousStop ( ) {
        return this.previousStop;
    }

    public double getLongitude ( ) {
        return this.longitude;
    }

    public double getLatitude ( ) {
        return this.latitude;
    }

    public void setId ( int nId ) {
        this.id = nId;
    }

    public void setLine ( int nLine ) {
        this.line = nLine;
    }

    public void setNextStop ( String nNext ) {
        this.nextStop = nNext;
    }

    public void setPreviousStop ( String nPrevious ) {
        this.previousStop = nPrevious;
    }

    public void setLongitude ( double nLongitude ) {
        this.longitude = nLongitude;
    }

    public void setLatitude ( double nLatitude ) {
        this.latitude = nLatitude;
    }

    public void setPosition ( double nLongitude, double nLatitude ) {
        this.longitude = nLongitude;
        this.latitude = nLatitude;
    }

    public String toString ( ) {
        return "Bus id: " + this.id
                + "; line: " + this.line
                + "; prev stop: " + this.previousStop
                + "; next stop: " + this.nextStop
                + "; longitude: " + this.longitude
                + "; latitude: " + this.latitude;
    }

    public JSONObject toJSONObject ( ) {
        JSONObject bus = new JSONObject ( );
        bus.put ( "number", this.line );
        bus.put ( "direction","null" );
        bus.put ( "latitude", this.latitude );
        bus.put ( "longitude", this.longitude );
        return bus;
    }
}
