package de.hsharz.hvb.database;

import org.apache.commons.dbcp2.*;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {

    private final Logger                        infoLog         = Logger.getLogger ( "info" );
    private final Logger                        errLog          = Logger.getLogger ( "error" );

    private DataSource                          dataSource      = null;
    private ObjectPool < PoolableConnection >   connectionPool  = null;

    //private XMLPropertiesLoader loader = null;

    private String dbDriver = null;
    private String dbUser = null;
    private String dbPass = null;
    private String dbUrl = null;

    public DBConnection ( ) {
        //this.loader = new XMLPropertiesLoader ( "/home/admin/sam/config/sammaster.xml" );
        this.loadProperties ( );
        this.init ( );
    } // public DBConnection ( )

    private void loadProperties ( ) {
        this.dbDriver = "com.mysql.jdbc.Driver";
        this.dbUrl = "jdbc:mysql://h2813041.stratoserver.net:3306/HVB";
        this.dbUser = "hvb";
        this.dbPass = "hvbguest";
        /*try {
            this.dbDriver = this.loader.get ( "dbDriver" );
            this.dbUrl = this.loader.get ( "dbUrl" );
            this.dbUser = this.loader.get ( "dbUser" );
            this.dbPass = this.loader.get ( "dbPass" );
        } // try
        catch ( IOException ioExc ) {
            log.error ( "Can not load properties.", ioExc );
            System.exit ( -1 );
        } // catch ( IOException ioExc )*/
    } // private void loadProperties ( )

    private void init ( ) {
        try {
            infoLog.info ( "Start initiating database connection." );
            infoLog.info ( "Load jdbc driver class" );
            Class.forName ( dbDriver );
            this.dataSource = this.setupDatasource ( );
            infoLog.info ( "Setting up done" );
            infoLog.info ( "Database initiated" );
        } // try
        catch ( ClassNotFoundException cnfExc ) {
            infoLog.error ( "Can not load jdbcDriver " + dbDriver + ".", cnfExc );
        } // private void init ( )
    } // private void init ( )

    private DataSource setupDatasource ( ) {
        infoLog.info ( "Setting up  data source." );

        GenericObjectPoolConfig config = new GenericObjectPoolConfig ( );
        config.setMaxTotal ( 20 );
        config.setMaxIdle ( 8 );
        config.setMinIdle ( 3 );
        config.setMaxWaitMillis ( 10000 );
        config.setMinEvictableIdleTimeMillis ( 2000 );

        ConnectionFactory connectionFactory
                = new DriverManagerConnectionFactory ( dbUrl, dbUser, dbPass );
        PoolableConnectionFactory poolableConnectionFactory
                = new PoolableConnectionFactory ( connectionFactory, null );
        this.connectionPool
                = new GenericObjectPool <> ( poolableConnectionFactory, config );
        poolableConnectionFactory.setPool ( this.connectionPool );
        PoolingDataSource < PoolableConnection > dataSource = new PoolingDataSource <> ( connectionPool );

        return dataSource;
    } // private DataSource setupDatasource ( )


    public Connection getConnection ( ) throws SQLException {
        this.printConnectionStatistics ( );
        return dataSource.getConnection ( );
    } // public Connection getConnection ( ) throws SQLException

    public void printConnectionStatistics ( ) {
        infoLog.info ( "Active: " + this.connectionPool.getNumActive ( ) );
        infoLog.info ( "Idle: " + this.connectionPool.getNumIdle ( ) );
    } // public void printConnectionStatistics ( )

}
