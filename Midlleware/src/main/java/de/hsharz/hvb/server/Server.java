package de.hsharz.hvb.server;

import de.hsharz.hvb.database.DBUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private final Logger    infoLog     = Logger.getLogger ( "info" );
    private final Logger    errLog      = Logger.getLogger ( "error" );

    public static DBUtils   database    = new DBUtils ( );

    private ServerSocket    server      = null;
    private Socket          client      = null;
    private ExecutorService pool        = null;

    private int             port;

    public Server ( int port ) {
        this.port = port;
        this.pool= Executors.newFixedThreadPool ( 5 );
    }

    public void run ( ) {
        try {
            infoLog.info ( "Entering server run method, initiate socket on port " + this.port );
            this.server = new ServerSocket ( this.port );
            while ( true ) {
                infoLog.info ( "Server waits for new connections........" );
                this.client = this.server.accept ( );
                infoLog.info ( "Established connection from client with ip-address "
                        + this.client.getRemoteSocketAddress ( ) );
                this.pool.execute ( new Worker ( this.client ) );
                infoLog.debug ( "New Thread created, turn back to waiting position." );
            } // while ( true )
        } // try
        catch ( IOException ioExc ) {
            if ( this.server != null ) {
                errLog.error ( "Can not connect to client, canceling attempt." );
            } // if ( this.server != null )
            else {
                errLog.error ( "Can not create server socket, exit program." );
                System.exit ( 1 );
            } // else
        } // catch ( IOException ioExc )
    } // public void run ( )
} // public class Server implements Runnable
