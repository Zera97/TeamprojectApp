package de.hsharz.hvb.database.wrapper;

import org.json.JSONObject;

public class BusStop {

    private double longitude;
    private double latitude;
    private int id;
    private String name;

    public BusStop ( int nId, String nName, double nLongitude, double nLatitude ) {
        this.id = nId;
        this.name = nName;
        this.longitude = nLongitude;
        this.latitude = nLatitude;
    }

    public BusStop ( ) {
    }

    public int getId ( ) {
        return this.id;
    }

    public String getName ( ) {
        return this.name;
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

    public void setName ( String nName ) {
        this.name = nName;
    }

    public void setLongitude ( double nLongitude ) {
        this.longitude = nLongitude;
    }

    public void setLatitude ( double nLatitude ) {
        this.latitude = nLatitude;
    }

    public String toString ( ) {
        return "Bus stop id: " + this.id
                + "; name: " + this.name
                + "; longitude: " + this.longitude
                + "; latitude: " + this.latitude;
    }

    public JSONObject toJSONObject ( ) {
        JSONObject json = new JSONObject ( );
        json.put ( "id", this.id );
        json.put ( "name",this.name );
        json.put ( "latitude", this.latitude );
        json.put ( "longitude", this.longitude );
        return json;
    }
}
