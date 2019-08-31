package de.hsharz.hvb.database;

import de.hsharz.hvb.database.wrapper.Bus;
import de.hsharz.hvb.database.wrapper.BusStop;
import de.hsharz.hvb.database.wrapper.Bussignal;
import de.hsharz.hvb.database.wrapper.StopTime;
import de.hsharz.hvb.helper.Column;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class DBUtils {


    //attribute der Datenbank werden hier initialisiert und deklariert
    private final String COL_ID         = "nr";
    private final String COL_BEZ        = "bez";
    private final String COL_RW         = "rw";
    private final String COL_HW         = "hw";
    private final String COL_TIME       = "time";
    private final String COL_DAY        = "wochentag";
    private final String COL_HID        = "hindex";
    private final String COL_LID        = "lindex";
    private final String COL_PID        = "pindex";
    private final String COL_LONGITUDE  = "longitude";
    private final String COL_LATITUDE   = "latitude";
    private final String COL_DIRECTION  = "direction";
    private final String COL_LNUMBER    = "l_nummer";
    private final String COL_NEXTSTOP   = "nextstop";
    private final String COL_NEXTTIME   = "nexttime";
    private final String COL_LASTSTOP   = "laststop";
    private final String COL_LASTTIME   = "lasttime";
    private final String COL_HST2       = "hstindex2";

    //nur Blackbox atribute
    private final String COL_BBID       = "bb_id";
    private final String COL_SIGNAL     = "signal";
    private final String COL_NETTYPE    = "nettype";

    //Logger Objekte für ausgaben
    private final Logger        infoLog         = Logger.getLogger ( "info" );
    private final Logger        errLog          = Logger.getLogger ( "error" );

    //Datenbank verbindungsattribute
    private DBConnection        pool            = null;
    private PreparedStatement   pstm            = null;
    private Connection          con             = null;
    private ResultSet           rs              = null;

    public DBUtils ( ) {
        this.pool = new DBConnection ( );
    } // public DBUtils ( )

    private String load ( String key ) throws IOException {
        InputStream fis = new FileInputStream ( "resources/sql.properties" );
        Properties props = new Properties ( );
        props.load ( fis );
        return props.getProperty ( key );
    }

    //Methode um alle Haltestelleninformationen zu bekommen
    //wird allerdings nicht genutzt
    public ArrayList < BusStop > selectAllBusStops ( ) {
        try {
            this.con = this.pool.getConnection ( );
            String query = this.load ( "selectbusstops" );
            this.pstm = con.prepareStatement ( query );

            ArrayList < BusStop > result = new ArrayList <> ( );
            this.rs = pstm.executeQuery ( );
            while ( rs.next ( ) ) {
                BusStop busStop = new BusStop ( );
                busStop.setId ( rs.getInt ( Column.NR.getColumn ( ) ) );
                busStop.setName ( rs.getString ( Column.BEZ.getColumn ( ) ) );
                busStop.setLongitude ( rs.getDouble ( Column.RW.getColumn ( ) ) );
                busStop.setLatitude ( rs.getDouble ( Column.HW.getColumn ( ) ) );
                result.add ( busStop );
            } // while ( rs.next ( ) )

            this.close ( );
            return result;
        } // try
        catch ( SQLException sqlExc ) {
            errLog.error ( "Can not select all bus stops.", sqlExc );
            return null;
        } // catch ( SQLException sqlExc )
        catch ( IOException ioExc ) {
            errLog.error ( "Can not load sql-query.", ioExc );
            return null;
        } // catch ( IOException ioExc )
    } // public ArrayList < BusStop > selectAllBusStops ( )

    //Methode die alle Verbindungen zur Datenbank beendet
    private void close ( ) throws SQLException {
        if ( this.con != null )
            this.con.close ( );
        if ( this.pstm != null )
            this.pstm.close ( );
        if ( this.rs != null )
            this.rs.close ( );
    } // private void close ( ) throws SQLException

    //Methode für alle Haltestellen
    public ArrayList < BusStop > selectAllBussstops ( ) {
        try {
            ArrayList < BusStop > result = new ArrayList ( );
            Connection con = this.pool.getConnection ( );
            Statement stmnt = con.createStatement ( );
            ResultSet rs = stmnt.executeQuery ( "SELECT * FROM HALTESTELLE;" );

            while ( rs.next ( ) ) {
                BusStop b = new BusStop ( );
                b.setId ( rs.getInt ( COL_ID ) );
                b.setName ( rs.getString ( COL_BEZ ) );
                b.setLongitude ( rs.getDouble ( COL_RW ) );
                b.setLatitude ( rs.getDouble ( COL_HW ) );
                result.add ( b );
            } // while ( rs.next ( ) )

            stmnt.close ( );
            rs.close ( );
            con.close ( );
            return result;
        } // try
        catch ( SQLException sqlExc ) {
            errLog.error ( "Can not select all bus stops.", sqlExc );
            return null;
        } // catch ( SQLException sqlExc )
    } // public ArrayList < BusStop > selectAllBussstops ( )

    public ArrayList < StopTime > selectBusstopInformationAdvanced ( int id, int time ) {
        return this.selectBusstopInformation ( id, time );
    }

    //Methode für mehr Informationen einer Haltestelle für jeden Bus der dort vorbei fährt
    public ArrayList < StopTime > selectBusstopInformation ( int id, int time ) {
        try {

            ArrayList < StopTime > result = new ArrayList ( );
            Connection con = this.pool.getConnection ( );
            Statement stmt = con.createStatement ( );

            //Query holt alle Linien die an dieser Haltestellen in der nächsten Stunde anhalten
            this.pool.printConnectionStatistics();
            String lineQuery = "SELECT t.LINDEX, l.DIRECTION FROM TERMIN t, LINIE l WHERE t.HSTINDEX = " + id
                    + " AND l.PINDEX = t.LINDEX GROUP BY LINDEX;";

            ResultSet lineRS = stmt.executeQuery(lineQuery);

            infoLog.info ( "begin of SQL-Statements" );
            ArrayList <StopTime> stopTimes = new ArrayList <StopTime> ( );
            while ( lineRS.next ( ) ){
                StopTime t = new StopTime ( );
                infoLog.info ( "before geting lineId" );
                t.setLine ( lineRS.getInt ( COL_LID ) );
                infoLog.info ( "before getting lineDirection" );
                t.setDirection ( lineRS.getString ( COL_DIRECTION ) );
                infoLog.info ( "before adding Stoptime to ArrayList" );
                stopTimes.add ( t );
                infoLog.info(t);
            }

            this.pool.printConnectionStatistics();

            ResultSet rsNextStop = null;
            Statement stmt2 = null;
            ResultSet rsNextStopTime = null;
            infoLog.info ( "entering Loop" );
            //schleife die alle Zurückgegebenen Linien durchläuft
            for(StopTime t : stopTimes){

                int lID = t.getLine();
                infoLog.info ( "Linien Id: " + lID );
                //Query holt die nächste Bushaltestelle der Linie

                String nextStopQuery = "SELECT HSTINDEX2 FROM ABSCHNITT a, STRECKE s "
                        + "WHERE s.ABSCHTINDEX = a.PINDEX AND a.HSTINDEX1 = " + id + " AND s.LINIEINDEX = " + lID + ";";
                rsNextStop = stmt.executeQuery(nextStopQuery);
                int nextStop;
                if(!rsNextStop.next( )) {
                    infoLog.info("Keine nächste Haltestelle gefunden!");
                    nextStop = -1;
                }else {
                    nextStop = rsNextStop.getInt(COL_HST2);
                    infoLog.info("nextStop Id: " + nextStop);
                }
                String nextStopTimeQuery = "SELECT TIME FROM TERMIN " +
                        "WHERE HSTINDEX = " + nextStop + " AND TIME >= " + time + " AND TIME <= " + time+60
                        + " AND LINDEX = " + lID + ";";

                stmt2 = con.createStatement();
                rsNextStopTime = stmt2.executeQuery(nextStopTimeQuery);

                int nextStopTime;
                if(!rsNextStopTime.next( )) {
                    infoLog.info("Keine Zeit für die nächste Haltestelle gefunden!");
                    nextStopTime = -1;
                }else {
                    nextStopTime = rsNextStopTime.getInt(COL_TIME);
                    infoLog.info( "nextStopTime: " + nextStopTime );
                }

                t.setId ( id );
                t.setTime ( time );
                t.setNextStop ( nextStop );
                t.setNextTime ( nextStopTime );

                result.add( t );
                infoLog.info( t );

            }//while ( rs.next ( ) )

            lineRS.close();
            stmt.close();
            rsNextStop.close();
            rsNextStopTime.close();
            con.close ( );
            return result;
        } // try
        catch ( SQLException sqlExc ) {
            errLog.error ( "Can not select dates or bus line information.", sqlExc );
            return null;
        } // catch ( SQLException sqlExc )

    } // public ArrayList< StopTime > selectBusstopInformation ( int id, int hours, int min )


    //Gibt die letzte bekannte position der Busse zurück
    public Bus selectLastBusPosition ( int line ) {
        try {
            Connection con = this.pool.getConnection ( );
            Statement stmt = con.createStatement ( );

            //SQL-Anfrage für alle Daten einer Line aus der Datenbank
            ResultSet rs = stmt.executeQuery ( "SELECT * FROM BB_DATA where L_NUMMER = " + line + ";" );
            Bus b = null;
            //überprüfen ob Daten in der Datenbank vorhanden sind und springt auf das letzte element für die letzte Position
            // wenn nicht vorhanden fehlermeldung einbauen
            if(rs.last ( ) ) {
                b = new Bus();
                b.setLine(rs.getInt(COL_LNUMBER));
                b.setLatitude(rs.getDouble(COL_LATITUDE));
                b.setLongitude(rs.getDouble(COL_LONGITUDE));
            }
            else {
                infoLog.warn ( "Position for bus " + line + " is not available." );
            }
            stmt.close ( );
            rs.close ( );
            con.close ( );

            return b;
        } // try
        catch ( SQLException sqlExc ) {
            errLog.error ( "Can not select bus position.", sqlExc );
            return null;
        } // catch ( SQLException sqlExc )
    } // public ArrayList < Bus > selectAllBusses ( )

    //Methode für WebService für alle BB-Daten
    public ArrayList < Bussignal > getBusSignal(){
        try {
            Connection con = this.pool.getConnection();
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM BB_DATA");
            ArrayList < Bussignal > result = new ArrayList <> ( );

            while( rs.next( ) ){
                Bussignal b = new Bussignal ( );
                b.setBBID( rs.getInt ( COL_BBID ) );
                b.setBusLine( String.valueOf ( rs.getInt ( COL_LNUMBER ) ) );
                b.setLatitude( rs.getDouble ( COL_LATITUDE ) );
                b.setLongitude( rs.getDouble ( COL_LONGITUDE ) );
                b.setSignal( rs.getString ( COL_SIGNAL ) );
                b.setType( rs.getString ( COL_NETTYPE ) );
                result.add(b);
            }

            return result;
        } catch (SQLException sqlExc) {
            errLog.error ( "Can not select signal information.", sqlExc );
            return null;
        }

    }

    public void insertBBData ( Bussignal busSignal ) {
        try {
            Connection con = this.pool.getConnection ( );
            Statement stmt = con.createStatement ( );

            //aktueller Timestamp wird geholt und in Datenbank mit anderen Attributen gespeichert
            Date date = new Date ( );
            long time = date.getTime ( );
            Timestamp ts = new Timestamp ( time );
            stmt.executeUpdate ( "insert into BB_DATA values ( "
                    + busSignal.getBBID ( ) + ", "
                    + busSignal.getBusLine ( ) + ", "
                    + busSignal.getLongitude ( ) + ", "
                    + busSignal.getLatitude ( ) + ", "
                    + busSignal.getSignal ( ) + ", "
                    + busSignal.getType ( ) + ", '"
                    + ts + "' );" );
            stmt.close ( );
            con.close ( );
        } // try
        catch ( SQLException sqlExc ) {
            errLog.error ( "Can not insert bus information.", sqlExc );
        } // catch ( SQLException sqlExc )
    } // public void insertBBData ( Bussignal busSignal )
} // public class DBUtils
