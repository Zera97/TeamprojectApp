package de.hsharz.hvb.database.wrapper;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public class StopTime {

    //time ist für die implementierung eines Ints in der db für die zeit
    int id, day = 31, time, nextTime, nextStop, lastTime, lastStop, line;
    String direction;

    private final Logger infoLog         = Logger.getLogger ( "info" );

    public StopTime ( int nId, int nDay ) {
        this.id = nId;
        this.day = nDay;
    }

    public StopTime ( ) {
    }

    public int getId ( ) {
        return this.id;
    }

    public int getDay ( ) {
        return this.day;
    }

    public int getTime ( ) { return this.time; }

    public int getLine ( ) { return this.line; }

    public String getDirection ( ) { return this.direction; }

    public int getNextStop ( ) { return this.nextStop; }

    public int getNextTime ( ) { return this.nextTime; }

    public int getLastStop ( ) { return this.lastStop; }

    public int getLastTime ( ) { return this.lastTime; }

    public void setId ( int nId ) {
        this.id = nId;
    }

    public void setDay ( int nDay ) {
        this.day = nDay;
    }

    public void setTime ( int nTime) { this.time = nTime; }

    public void setLine ( int nLine ) { this.line = nLine; }

    public void setDirection ( String nDirection ) { this.direction = nDirection; }

    public void setNextStop ( int nStop ) { this.nextStop = nStop; }

    public void setNextTime ( int nTime ) { this.nextTime = nTime; }

    public void setLastStop ( int nStop ) { this.lastStop = nStop; }

    public void setLastTime ( int nTime ) { this.lastTime = nTime; }

    public JSONObject toJSONObject ( ) {
        JSONObject stopTime = new JSONObject ( );
        stopTime.put ( "line", this.line );
        stopTime.put ( "id", this.id );
        stopTime.put ( "time", this.time );
        stopTime.put ( "day", this.day );
        stopTime.put ( "nextstop", this.nextStop );
        stopTime.put ( "nexttime", this.nextTime );
        stopTime.put ( "direction", this.direction );
        stopTime.put ( "laststop", this.lastStop );
        stopTime.put ( "lasttime", this.lastTime );
        infoLog.info ( stopTime.toString ( ) );
        return stopTime;
    } // public JSONObject toJSONObject ( )

    public String toString ( ) {
        return "lineId: " + this.line + "\n Direction: " + this.direction;
    }

}
