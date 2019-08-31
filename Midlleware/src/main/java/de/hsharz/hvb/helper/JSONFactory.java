package de.hsharz.hvb.helper;

import org.json.JSONObject;

/**
 * Simple helper-factory class to create json response messages.
 */
public class JSONFactory {

    public static JSONObject createJSONHeader ( JSONObject received ) {
        if ( received.has ( "id" ) && received.has ( "code" ) ) {
            JSONObject json = new JSONObject ( );
            json.put ( "id", received.getInt ( "id" ) );
            json.put ( "code", received.getInt ( "code" ) );
            json.put ( "sender", "core" );
            return json;
        } // if ( received.has ( "id" ) && received.has ( "code" ) )
        else {
            return JSONFactory.createErrorResponse ( 2 );
        } // else
    } // public static JSONObject createJSONHeader ( JSONObject received )

    public static JSONObject createJSONHeaderForTest ( JSONObject received ) {
        if ( received.has ( "id" ) && received.has ( "code" ) && received.has ( "data" ) ) {
            JSONObject json = new JSONObject ( );
            json.put ( "id", received.getInt ( "id" ) );
            json.put ( "code", received.getInt ( "code" ) );
            json.put ( "sender", "core" );
            json.put ( "data", ( received.getInt ( "data" ) + 1 ) );
            return json;
        } // if ( received.has ( "id" ) && received.has ( "code" ) && received.has ( "data" ) )
        else {
            return JSONFactory.createErrorResponse ( 2 );
        } // else
    } // public static JSONObject createJSONHeaderForTest ( JSONObject received )

    public static JSONObject createErrorResponse ( int errorCode ) {
        JSONObject json = new JSONObject ( );
        json.put ( "sender", "core" );
        json.put ( "id", -1 );
        json.put ( "error", errorCode );
        return json;
    } // public static JSONObject createErrorResponse ( int errorCode )
} // public class JSONFactory
