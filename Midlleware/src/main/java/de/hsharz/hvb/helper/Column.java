package de.hsharz.hvb.helper;

public enum Column {

    ID ( "id" ),
    BBID ( "bb_id" ),
    PID ( "pindex" ),

    BEZ ( "bez" ),
    NR ( "nr" ),
    HW ( "hw" ),
    RW ( "rw" ),

    LNR ( "l_nummer" ),

    LONGITUDE ( "longitude" ),
    LATITUDE ( "latitude" ),
    DIRECTION ( "direction" ),

    TIME ( "time" ),

    NEXTSTOP ( "nextstop" ),
    NEXTTIME ( "nexttime" ),
    LASTSSTOP ( "laststop" ),
    LASTSTIME ( "lasttime" ),

    TYPE ( "nettype" ),
    SIGNAL ( "signal" );

    private String column;

    private Column ( String column ) {
        this.column = column;
    }

    public String getColumn ( ) {
        return this.column;
    }

    public String toString ( ) {
        return this.column;
    }
}
