package de.hsharz.hvb.handler;

import de.hsharz.hvb.database.DBUtils;
import de.hsharz.hvb.database.wrapper.Bussignal;
import de.hsharz.hvb.handler.interfaces.Handler;
import de.hsharz.hvb.helper.JSONFactory;
import de.hsharz.hvb.server.Server;
import org.apache.log4j.Logger;
import org.json.JSONObject;

public class BoxHandler implements Handler {

    private final Logger        infoLog     = Logger.getLogger ( "info" );
    private final Logger        errLog      = Logger.getLogger ( "error" );
    private final Logger        sigLog      = Logger.getLogger ( "signal" );

    private JSONObject          request     = null;
    private JSONObject          response    = null;


    public BoxHandler ( JSONObject request ) {
        this.request = request;
    } // public BoxHandler ( JSONObject request )

    public JSONObject run ( ) {
        infoLog.info ( "Enter run method of box-handler-class." );
        if ( this.request.has ( "code" ) ) {
            switch ( this.request.getInt ( "code" ) ) {
                default:
                    infoLog.warn ( "Unable to determine message code. Nothing to do." );
                    break;
                case 0:
                    infoLog.info ( "Determined request for communication test." );
                    this.test ( );
                    break;
                case 1:
                    infoLog.info ( "Determined request for inserting signal information." );
                    this.treatingBusSignal ( );
                    break;
            } // switch ( this.request.getInt ( "code" ) )
        } // if ( this.request.has ( "code" ) )
        else {
            this.response = JSONFactory.createErrorResponse ( 3 );
        } // else
        infoLog.info ( "Leave run method of box-handler-class properly." );
        return this.response;
    } // public JSONObject run ( )

    private void treatingBusSignal ( ) {
        if ( this.request.has ( "boxId" ) && this.request.has ( "line" )
                && this.request.has ( "longitude" ) && this.request.has ( "latitude" )
                && this.request.has ( "signal" ) && this.request.has ( "nettype" ) ) {
            Bussignal signal = new Bussignal ( this.request );
            sigLog.info ( signal );
            Server.database.insertBBData ( signal );

            this.response = JSONFactory.createJSONHeader ( this.request );
        } // if ( this.request.has ( "boxId" ) && this.request.has ( "line" )
        // && this.request.has ( "longitude" ) && this.request.has ( "latitude" )
        // && this.request.has ( "signal" ) && this.request.has ( "nettype" ) )
        else {
            this.response = JSONFactory.createErrorResponse ( 4 );
        } // else
        infoLog.info ( "Processing signal insertion finished." );
    } // private void treatingBusSignal ( )

    private void test ( ) {
        this.response = JSONFactory.createJSONHeaderForTest ( this.request );
        infoLog.info ( "Processing communication test finished." );
    } // private void test ( )
} // public class BoxHandler implements Handler
