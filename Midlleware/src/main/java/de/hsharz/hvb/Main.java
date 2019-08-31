package de.hsharz.hvb;

import de.hsharz.hvb.server.Server;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Main {

    private static final Logger     infoLog     = Logger.getLogger ( "info" );

    /**
     * Staring point of the hvb-core. Depending on given parameters, the server starts either on the
     * default port 31896 or on a port given by the parameters.
     *
     * @param args::String - arguments passed by call of the jar-file
     */
    public static void main ( String [ ] args ) {

        infoLog.info ( "\n\n" );
        infoLog.info ( "#############################################################" );
        infoLog.info ( "#                                                           #" );
        infoLog.info ( "#                   HVB-Core started                        #" );
        infoLog.info ( "#                                                           #" );
        infoLog.info ( "#############################################################" );
        infoLog.info ( "\n\n" );

        int port = 31896;
        PropertyConfigurator.configureAndWatch ( "resources/log4j.properties" );
        Server s = null;
        if ( args.length > 0 ) {
            port = Integer.valueOf ( args [ 0 ] );
        } // if ( args.length > 0 )
        s = new Server ( port );
        infoLog.info ( "Creating server on port " + port );
        s.run ( );
    } // public static void main ( String [ ] args )
} // public class Main
