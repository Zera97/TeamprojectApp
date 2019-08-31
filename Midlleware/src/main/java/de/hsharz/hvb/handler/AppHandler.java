package de.hsharz.hvb.handler;

import de.hsharz.hvb.database.DBUtils;
import de.hsharz.hvb.database.wrapper.Bus;
import de.hsharz.hvb.database.wrapper.BusStop;
import de.hsharz.hvb.database.wrapper.StopTime;
import de.hsharz.hvb.handler.interfaces.Handler;
import de.hsharz.hvb.helper.JSONFactory;
import de.hsharz.hvb.server.Server;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 *  Handler class implements the handler interface for processing all kind of request received from any app clients.
 *  As the implemented interface tells the run-method must be implemented. Only the run-method delegates the requests
 *  to their tasks. The special tasks processes the requests and generates responses.
 */
public class AppHandler implements Handler {

    private final Logger            infoLog     = Logger.getLogger ( "info" );
    private final Logger            errLog      = Logger.getLogger ( "error" );

    private JSONObject              request     = null;
    private JSONObject              response    = null;
    private ArrayList < BusStop >   busstops    = null;
    private ArrayList < StopTime>   stoptimes   = null;
    private ArrayList < Bus >       busses      = null;

    public AppHandler ( JSONObject request ) {
        this.request = request;
    } // public AppHandler ( JSONObject received )

    public JSONObject run ( ) {
        if ( this.request.has ( "code" ) ) {
            switch ( this.request.getInt ( "code" ) ) {
                default:
                    infoLog.info ( "Determined request for communication test." );
                    this.test ( );
                    break;
                case 1:
                    infoLog.info ( "Determined request for all bus stop positions." );
                    this.getAllBusstops ( );
                    break;
                case 2:
                    infoLog.info ( "Determined request for all bus positions." );
                    if ( this.request.has ( "selection" ) ) {
                        JSONArray jsonArray = this.request.getJSONArray ( "selection" );
                        if ( ( jsonArray != null ) && ( jsonArray.length ( ) > 0 ) ) {
                            int [ ] lines = new int [ jsonArray.length ( ) ];
                            for ( int i = 0; i < jsonArray.length ( ); i++ ) {
                                infoLog.info ( "Requested for line " + jsonArray.getInt ( i ) );
                                lines[ i ] = jsonArray.getInt ( i );
                            } // for ( int i = 0; i < jsonArray.length ( ); i++ )
                            infoLog.info ( "Request contains bus line information." );
                            this.getBusesForLine ( lines );
                        }  // if ( ( jsonArray != null ) && ( jsonArray.length ( ) > 0 ) )
                        else {
                            infoLog.info ( "Request did not contain bus line information." );
                            this.getBuses ( );
                        } // else
                    } // if ( this.request.has ( "selection" ) )
                    else {
                        this.response = JSONFactory.createErrorResponse ( 4 );
                    } // else
                    break;
                case 3:
                    infoLog.info ( "Determined request for special bus stop information." );
                    if ( this.request.has ( "stopID" ) && this.request.has ( "time" ) ) {
                        this.getBusServices ( this.request.getInt ( "stopID" ), this.request.getInt ( "time" ) );
                    } // if ( this.request.has ( "stopID" ) && this.request.has ( "time" ) )
                    else {
                        this.response = JSONFactory.createErrorResponse ( 4 );
                    } // else
                    break;
                case 4:
                    infoLog.info ( "Determined request for advanced bus stop information." );
                    if ( this.request.has ( "stopID" ) && this.request.has ( "time" ) ) {
                        this.getAdvancedBusServices ( this.request.getInt ( "id" ), this.request.getInt ( "time" ) );
                    } // if ( this.request.has ( "stopID" ) && this.request.has ( "time" ) )
                    else {
                        this.response = JSONFactory.createErrorResponse ( 4 );
                    } // else
                    break;
                case 5:
                    infoLog.info ( "Determined request for route time information." );
                    if ( this.request.has ( "longitude" ) && this.request.has ( "latitude" )
                            && this.request.has ( "stopID" ) && this.request.has ( "connectionID" ) ) {
                        double longitude = this.request.getDouble ( "longitude" );
                        double latitude = this.request.getDouble ( "latitude" );
                        int id = this.request.getInt ( "stopID" );
                        int conId = this.request.getInt ( "connectionID" );
                        this.getTimeToStart ( id, conId, longitude, latitude );
                    } // if ( this.request.has ( "longitude" ) && this.request.has ( "latitude" )
                    // && this.request.has ( "stopID" ) && this.request.has ( "connectionID" ) )
                    else {
                        this.response = JSONFactory.createErrorResponse ( 4 );
                    } // else
                    break;
            } // switch ( this.request.getInt ( "code" ) )
        } // if ( this.request.has ( "code" ) )
        else {
            this.response = JSONFactory.createErrorResponse ( 3 );
        } // else
        return this.response;
    } // public JSONObject run ( )

    private void getAllBusstops ( ) {
        this.response = JSONFactory.createJSONHeader ( this.request );

        this.busstops = Server.database.selectAllBussstops ( );
        JSONArray jsonBusstops = new JSONArray ( );
        for ( BusStop busstop : busstops ) {
            jsonBusstops.put ( busstop.toJSONObject ( ) );
        } // for ( BusStop busstop : busstops )

        this.response.put ( "busstops", jsonBusstops );
        infoLog.info ( "Processing bus stops request finished." );
    } // private void getAllBusstops ( )

    /**
     * Handles code 2 requests when request message contains at least one bus line number.
     * Only buses with given bus line number are getting selected.
     *
     * @param lines:: Integer Array: Array with all requested bus line numbers.
     */
    private void getBusesForLine ( int [ ] lines ) {
        this.response = JSONFactory.createJSONHeader ( this.request );

        JSONArray jsonBuses = new JSONArray ( );
        for ( int i = 0; i < lines.length; i++ ) {
            Bus bus = Server.database.selectLastBusPosition ( lines [ i ] );
            if ( bus == null ) {
                bus = this.fillPseudoPositions ( lines[ i ] );
                infoLog.warn ( "No bus position for line " + i + " in database. Filled response with pseudo information." );
            } // if ( bus == null )
            jsonBuses.put ( bus.toJSONObject ( ) );
        }

        this.response.put ( "bus", jsonBuses );
        infoLog.info ( "Processing bus position request with defined bus lines finished." );
    } // private void getBusesForLine ( )

    /**
     * Handles code 2 requests when request messages has no bus line number.
     * All saved buses are getting selected.
     */
    private void getBuses ( ) {
        this.response = JSONFactory.createJSONHeader ( this.request );

        JSONArray jsonBuses = new JSONArray ( );
        for ( int i = 201; i < 205; i++ ) {
            Bus bus = Server.database.selectLastBusPosition ( i );
            if ( bus == null ) {
                bus = this.fillPseudoPositions ( i );
                infoLog.warn ( "No bus position for line " + i + " in database. Filled response with pseudo information." );
            } // if ( bus == null )
            jsonBuses.put ( bus.toJSONObject ( ) );
        } // for ( int i = 201; i < 205; i++ )

        this.response.put ( "bus", jsonBuses );
        infoLog.info ( "Processing bus position request for all lines finished." );
    } // private void getBuses ( )

    private void getBusServices ( int id, int time ) {
        this.response = JSONFactory.createJSONHeader ( this.request );
        this.stoptimes = Server.database.selectBusstopInformation ( id, time );
        if ( this.stoptimes != null && this.stoptimes.size ( ) > 0 ) {
            JSONArray jsonStopTimes = new JSONArray ( );
            for ( StopTime stopTime : this.stoptimes ) {
                jsonStopTimes.put ( stopTime.toJSONObject ( ) );
            } // for ( StopTime stopTime : this.stoptimes )

            this.response.put ( "stoptimes", jsonStopTimes );
        } // if ( this.stoptimes != null && this.stoptimes.size ( ) > 0 )
        else {
            infoLog.warn ( "Did not found any data in database." );
        } // else
        infoLog.info ( "Processing bus stop information request for 3 lines finished." );
    } // private void getBusServices ( int id, int time )

    private void getAdvancedBusServices ( int id, int time ) {
        this.response = JSONFactory.createJSONHeader ( this.request );

        this.stoptimes = Server.database.selectBusstopInformationAdvanced ( id, time );
        JSONArray jsonStopTimes = new JSONArray ( );
        for ( StopTime stopTime : this.stoptimes ) {
            jsonStopTimes.put ( stopTime.toJSONObject ( ) );
        } // for ( StopTime stopTime : this.stoptimes )

        this.response.put ( "stoptimes", jsonStopTimes );
        infoLog.info ( "Processing bus stop information request for all lines finished." );
    } // private void getAdvancedBusServices ( int id, int time )

    private void getTimeToStart ( int id, int conId, double longitude, double latitude ) {
        this.response = JSONFactory.createJSONHeader ( this.request );

        this.response.put ( "timeTillArrival", ThreadLocalRandom.current ( ).nextInt ( 1, 182 ) );
        int delay = ThreadLocalRandom.current ( ).nextInt ( 1, 10 );
        int random = ThreadLocalRandom.current ( ).nextInt ( 0, 1 );
        if ( random == 0 )
            delay *= -1;

        this.response.put ( "delayBus", delay );
        this.response.put ( "TimeWalk", ThreadLocalRandom.current ( ).nextInt ( 5, 30 ) );
        infoLog.info ( "Processing route time information request finished." );
    } // private void getTimeToStart ( int id, int conId, double longitude, double latitude )

    private Bus fillPseudoPositions ( int line ) {
        Bus bus = new Bus ( );
        bus.setId ( ThreadLocalRandom.current ( ).nextInt ( 1, 150 ) );
        bus.setLine ( line );
        bus.setLongitude ( 10.781021118164062 );
        bus.setLatitude ( 51.84109409457281 );
        bus.setNextStop ( "Pseudodata" );
        bus.setPreviousStop ( "Pseudodata" );
        return bus;
    } // private Bus fillPseudoPositions ( int line )

    private void test ( ) {
        this.response = JSONFactory.createJSONHeaderForTest ( this.request );
        infoLog.info ( "Processing communication test finished." );
    } // private void test ( )

} // public class AppHandler implements Handler
