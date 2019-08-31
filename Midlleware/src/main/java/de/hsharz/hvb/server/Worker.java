package de.hsharz.hvb.server;

import de.hsharz.hvb.database.DBUtils;
import de.hsharz.hvb.handler.AppHandler;
import de.hsharz.hvb.handler.BoxHandler;
import de.hsharz.hvb.handler.WebHandler;
import de.hsharz.hvb.handler.interfaces.Handler;
import de.hsharz.hvb.helper.ErrorCodes;
import de.hsharz.hvb.helper.JSONFactory;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

/**
 * The worker class delegates the incoming requests to their handlers depending on the requesting client.
 * The handlers return the generated responses so the worker can answer the client with a matching response or
 * if something went wrong a matching error message.
 */
public class Worker implements Runnable {

    private final Logger        infoLog     = Logger.getLogger ( "info" );
    private final Logger        errLog      = Logger.getLogger ( "error" );

    private Socket              client      = null;

    private OutputStream        os          = null;
    private InputStream         is          = null;
    private OutputStreamWriter  osr         = null;
    private InputStreamReader   isr         = null;
    private PrintWriter         pr          = null;
    private BufferedReader      br          = null;


    private Handler             handler     = null;

    private JSONObject          request     = null;
    private JSONObject          response    = null;

    public Worker ( Socket client ) {
        this.client = client;
    }

    public void run ( ) {
        infoLog.info ( "Enter run method of worker-class." );
        int debug = 0;
        try {
            this.receive ( );

            if ( request.has ( "sender" ) || request.has ( "Sender" ) ) {
                switch ( request.getString ( "sender" ).toLowerCase ( ) ) {
                    default:
                        infoLog.warn ( "Unable to determine target and handler. Nothing to do." );
                        break;
                    case "app":
                        infoLog.info ( "Start app handler to process request." );
                        this.handler = new AppHandler ( this.request );
                        this.response = this.handler.run ( );
                        break;
                    case "box":
                        infoLog.info ( "Start box handler to process request." );
                        this.handler = new BoxHandler ( this.request );
                        this.response = this.handler.run ( );
                        break;
                    case "web":
                        infoLog.info ( "Start web handler to process request." );
                        this.handler = new WebHandler ( this.request );
                        this.response = this.handler.run ( );
                        break;
                } // switch ( request.getString ( "sender" ) )
            }
            else {
                this.response = JSONFactory.createErrorResponse ( 1 );
            }
            debug++;
            this.send ( );

            debug++;
            this.close ( );
            infoLog.info ( "Leave run method of worker-class properly." );
        } // try
        catch ( IOException ioExc ) {
            switch ( debug ) {
                case 0:
                    errLog.error ( "Can not receive data from client.", ioExc );
                    break;
                case 1:
                    errLog.error ( "Can not send data " + this.response.toString ( ) + " to client.", ioExc );
                    break;
                case 2:
                    errLog.error ( "Can not close streams.", ioExc );
                    break;
            }
            infoLog.info ( "Leave run method of worker-class with error." );
        } // catch ( IOException ioExc )
    } // public void run ( )

    private void close ( ) throws IOException {
        if ( this.is != null )
            this.is.close ( );
        if ( this.isr != null )
            this.isr.close ( );
        if ( this.os != null )
            this.os.close ( );
        if ( this.osr != null )
            this.osr.close ( );
        if ( this.br != null )
            this.br.close ( );
        if ( this.pr != null )
            this.pr.close ( );
    } // private void close ( ) throws IOException

    private void receive ( ) throws IOException {
        infoLog.info ( "Initiate input-streams" );
        if ( this.is == null )
            this.is = this.client.getInputStream ( );
        if ( this.isr == null )
            this.isr = new InputStreamReader ( this.is );
        if ( this.br == null )
            this.br = new BufferedReader ( this.isr );

        this.request = new JSONObject ( this.br.readLine ( ) );
        infoLog.debug ( "Received request " + this.request.toString ( ) );
    } // private void receive ( ) throws IOException

    private void send ( ) throws IOException {
        infoLog.info ( "Initiate output-streams" );
        if ( this.os == null )
            this.os = this.client.getOutputStream ( );
        if ( this.osr == null )
            this.osr = new OutputStreamWriter ( this.os );
        if ( this.pr == null )
            this.pr = new PrintWriter ( this.osr );

        this.pr.println ( this.response.toString ( ) );
        this.pr.flush ( );
        infoLog.debug ( "Send response " + this.response.toString ( ) );
    } // private void send ( ) throws IOException
} // public class Worker implements Runnable
