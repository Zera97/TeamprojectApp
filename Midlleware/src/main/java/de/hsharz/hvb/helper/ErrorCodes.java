package de.hsharz.hvb.helper;

/**
 * This enum contains all possible error codes. So if a request message has a wrong key or value an error message
 * will be generated and gets the matching error code. While it will be easier to create error messages, it also
 * gives the possibility to tell the requesting client what went wrong.
 */
public enum ErrorCodes {

    M100 ( "General error has occurred" ),
    M101 ( "Unknown message code" ),
    M102 ( "Unknown sender" ),
    M103 ( "Message id is missing" );

    private String error = null;

    ErrorCodes ( String error ) {
        this.error = error;
    }

    public String getError ( ) {
        return error;
    }
}
