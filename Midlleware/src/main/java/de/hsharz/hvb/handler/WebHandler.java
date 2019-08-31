package de.hsharz.hvb.handler;

import de.hsharz.hvb.database.DBUtils;
import de.hsharz.hvb.database.wrapper.Bussignal;
import de.hsharz.hvb.handler.interfaces.Handler;
import de.hsharz.hvb.helper.ErrorCodes;
import de.hsharz.hvb.helper.JSONFactory;
import de.hsharz.hvb.server.Server;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class WebHandler implements Handler {

    private final Logger        infoLog     = Logger.getLogger ( "info" );
    private final Logger        errLog      = Logger.getLogger ( "error" );

    private JSONObject          request     = null;
    private JSONObject          response    = null;


    public WebHandler ( JSONObject request ) {
        this.request = request;
    } // public WebHandler ( JSONObject request )

    @Override
    public JSONObject run ( ) {
        infoLog.info ( "Enter run method of web-handler-class." );
        switch ( this.request.getInt ( "code" ) ) {
            default:
                infoLog.warn ( "Unable to determine message code. Nothing to do." );
                break;
            case 0:
                infoLog.info ( "Determined request for communication test." );
                this.test ( );
                break;
            case 1:
                infoLog.info ( "Determined request for signal information." );
                this.handleSignalRequest ( );
                break;
        } // switch ( this.request.getInt ( "code" ) )
        infoLog.info ( "Leave run method of web-handler-class properly." );
        return this.response;
    } // public JSONObject run ( )

    private void handleSignalRequest ( ) {
        this.response = JSONFactory.createJSONHeader ( this.request );

        ArrayList < Bussignal > signals = Server.database.getBusSignal ( );
        JSONArray jsonSignals = new JSONArray ( );
        for ( Bussignal signal : signals ) {
            jsonSignals.put ( signal.toJSONObject ( ) );
        } // for ( Bussignal signal : signals )

        this.response.put ( "bbinfo", jsonSignals );
        infoLog.info ( "Processing signal request finished." );
    } // private void handleSignalRequest ( )

    private void test ( ) {
        this.response = JSONFactory.createJSONHeaderForTest ( this.request );
        infoLog.info ( "Processing communication test finished." );
    } // private void test ( )

} // public class WebHandler implements Handler
