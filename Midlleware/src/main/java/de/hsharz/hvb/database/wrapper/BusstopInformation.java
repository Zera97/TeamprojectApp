    package de.hsharz.hvb.database.wrapper;

import java.util.ArrayList;

public class BusstopInformation {

    private int id;
    private int line;
    private ArrayList < StopTime > stops = null;

    public BusstopInformation ( int nId, ArrayList <StopTime > nStops, int nLine ) {
        this.id = nId;
        this.stops = nStops;
        this.line = nLine;
    }


    public int getId ( ) {
        return this.id;
    }

    public ArrayList < StopTime > getStops ( ) {
        return this.stops;
    }

    public ArrayList < StopTime > getStopsFromHour ( int time ) {
        ArrayList < StopTime > result = new ArrayList <> ( );
        for ( StopTime st : stops ) {
            if ( st.getTime() == time ) {
                result.add ( st );
            }
        }
        return result;
    }

    //public ArrayList < StopTime > getNextStops ( int hour, int minute ) {
    //}

    public int getLine ( ) { return this.line; }

    public void setStops ( ArrayList < StopTime > nStops ) {
        this.stops = nStops;
    }

    public void setId ( int nId ) {
        this.id = nId;
    }


    public void setLine ( int nLine) { this.line = nLine; }

    public String toString ( ) {
        return "BusstopInformation id: " + this.id
                + "; busline" + this.line;
    }
}