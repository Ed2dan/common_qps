package com.paxar.qps.orderhistory;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.io.Serializable;

public class Order implements Serializable
{
    static final long serialVersionUID = 2940928531139029468L;

    protected SimpleDateFormat format = new SimpleDateFormat( "yyyy-MMM-dd HH:mm:ss" );

    public static String SQL_BASE =
            "SELECT order_history.user_name, order_history.retailer, " +
            "       order_history.order_file, order_history.order_date, " +
            "       order_history.po, order_history.service_bureau " +
            "FROM order_history";

    public Order( ResultSet rs ) throws Exception
    {
        user = rs.getString( 1 );
        retailer = rs.getString( 2 );
        orderFile = rs.getString( 3 );
        orderDate = rs.getTimestamp( 4 );
        po = rs.getString( 5 );
        serviceBureau = rs.getString( 6 );
    }

    String po = null;
    public String getPO( )
    {
        if ( po == null ) { po = ""; }
        return po;
    }

    public String getUser( )
    {
        return user;
    }
    protected String user = null;
    public String getRetailer( )
    {
        return retailer;
    }
    protected String retailer = null;

    public String getOrderString( String serverBase ) throws Exception
    {
        String html = "";
        String line = null;
        File orderFile = getFile( serverBase );
        BufferedReader reader = new BufferedReader( new FileReader( orderFile ));
        do
        {
            line = reader.readLine( );
            if ( line != null )
            {
                html += line + "\n";
            }
        }
        while ( line != null );
        return html;
    }

    public File getFile( String serverBase )
    {
        return new File( OrderHistoryDAO.getOrderDirectory( serverBase, retailer ) +
                         File.separator + orderFile );
    }
    protected String orderFile = null;

    public String getDate( )
    {
        return format.format( orderDate ).toUpperCase( );
    }
    protected Date orderDate = null;

    public String getServiceBureau( )
    {
        return serviceBureau;
    }
    protected String serviceBureau = null;

    public String toString( )
    {
        return getDate( ) + "\t" + getRetailer( ) + "\t" + getServiceBureau( ) + "\t" + getUser( );
    }
}
