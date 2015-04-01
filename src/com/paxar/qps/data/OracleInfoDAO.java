package com.paxar.qps.data;

import java.sql.ResultSet;
import java.util.Vector;
import com.paxar.sql.Esc;

public class OracleInfoDAO extends AbstractDAO
{
	public static OracleInfo[] getInfo( String user, String retailer ) throws Exception
	{
		checkConnection( );

		String sql = "";

		sql = "SELECT user_billing.*, '', '', '', '', '', '', '', '', '' " +
              " ,'','','','','','','','','','' " +
			  "FROM user_billing " +
			  "WHERE user_billing.user_name = '" + Esc.escape( user ) + "' " +
			  "AND UPPER( user_billing.retailer ) = '" + Esc.escape( retailer ).toUpperCase( ) + "' " +
			  "ORDER BY user_billing.bill_to_cust_name ASC";
		ResultSet rs = connection.executeQuery( "paxar_profile", sql );
		Vector results = new Vector( );
		while ( rs.next( ))
		{
			results.add( new OracleInfo( rs ));
		}

		OracleInfo array[] = new OracleInfo[results.size( )];
		results.copyInto( array );
		return array;
	}

	public static FreightTerms[] getFreightTerms( String retailer ) throws Exception
	{
		checkConnection( );

		String sql = "";

		sql = "SELECT freight_terms.* FROM freight_terms " +
	      	  "WHERE UPPER( freight_terms.retailer ) = '" + Esc.escape( retailer ).toUpperCase( ) + "' " +
			  "ORDER BY freight_terms.freight_terms ASC";
		ResultSet rs = connection.executeQuery( "paxar_profile", sql );
		Vector results = new Vector( );
		while ( rs.next( ))
		{
			results.add( new FreightTerms( rs ));
		}

		FreightTerms array[] = new FreightTerms[results.size( )];
		results.copyInto( array );
		return array;
	}

	public static ShipVia[] getShipVia( String retailer ) throws Exception
	{
		checkConnection( );

		String sql = "";

		sql = "SELECT DISTINCT ship_method.* FROM ship_method " +
		      "WHERE UPPER( ship_method.retailer ) = '" + Esc.escape( retailer ).toUpperCase( ) + "' " +
			  "ORDER BY ship_method.ship_via ASC";
		ResultSet rs = connection.executeQuery( "paxar_profile", sql );
		Vector results = new Vector( );
		while ( rs.next( ))
		{
			results.add( new ShipVia( rs ));
		}

		ShipVia array[] = new ShipVia[results.size( )];
		results.copyInto( array );
		return array;
	}

    public static void updateEmail( String user, String email ) throws Exception
    {
        checkConnection( );

        Vector transaction = new Vector( 2 );
        String sql = "DELETE FROM contractor_email " +
                     "WHERE user_name = '" + user + "'";
        transaction.add( sql );
        sql = "INSERT INTO contractor_email ( user_name, email ) " +
              "VALUES ( '" + user + "', '" + email + "' )";
        transaction.add( sql );

        connection.executeUpdate( "qps", transaction );
    }

    public static String getEmail( String user ) throws Exception
    {
    	checkConnection( );

    	String email = "";
    	String sql = "";

        sql = "SELECT email FROM contractor_email WHERE " +
        "user_name = '" + user + "' LIMIT 1";
        ResultSet rs = connection.executeQuery( "qps", sql );
        if ( rs.next( ))
        {
        	email = rs.getString( 1 );
        }
        rs.close( );
        return email;
    }

    public static OracleOperatingUnit getOperatingUnit( String serviceBureau ) throws Exception
    {
    	checkConnection( );

    	String sql = "";

    	sql = "SELECT sb_operating_unit_id.* FROM sb_operating_unit_id " +
    		  "WHERE sb_operating_unit_id.service_bureau = '" + Esc.escape( serviceBureau ) + "' LIMIT 1";
    	ResultSet rs = connection.executeQuery( "paxar_profile", sql );
    	if ( rs.next( ))
    	{
    		OracleOperatingUnit unit = new OracleOperatingUnit( rs );
    		rs.close( );
    		return unit;
    	}
    	rs.close( );
    	throw new RuntimeException( "paxar_profile:sb_operating_unit_id " +
    							    "missing entry for " + serviceBureau + "." );

    }

    public static long getNextOrderNumber( String username ) throws Exception
    {
    	checkConnection( );

    	long orderNumber = 0;

    	String sql = "INSERT INTO d2comm_order_number ( username, order_number ) " +
    				 "VALUES ( '" + Esc.escape( username ) + "', DEFAULT )";
    	connection.executeUpdate( "paxar_profile", sql );

    	sql = "SELECT currval( 'd2comm_order_number_order_number_seq' )";
    	ResultSet rs = connection.executeQuery( "paxar_profile", sql );
    	if ( rs.next( ))
    	{
    		orderNumber = rs.getLong( 1 );
    		rs.close( );
    	} else
    	{
    		rs.close( );
    		throw new RuntimeException( "Unable to get d2comm order number." );
    	}
    	return orderNumber;
    }

    public static String getDefaultShipping( String user ) throws Exception
    {
    	checkConnection( );

    	String code = "";

    	String sql = "";
    	sql = "SELECT ship_method_defaults.ship_code " +
    		  "FROM ship_method_defaults " +
    		  "WHERE ship_method_defaults.username = '" + Esc.escape( user ) + "' " +
    		  "LIMIT 1";
    	ResultSet rs = connection.executeQuery( "paxar_profile", sql );
    	if ( rs.next( ))
    	{
    		code = rs.getString( 1 );
    	}
    	rs.close( );
    	return code;
    }

    public static void setDefaultShipping( String user, String code ) throws Exception
    {
    	checkConnection( );

    	Vector transaction = new Vector( );

    	String sql = "";
    	sql = "DELETE FROM ship_method_defaults " +
    		  "WHERE username = '" + Esc.escape( user ) + "'";
    	transaction.add( sql );

    	sql = "INSERT INTO ship_method_defaults ( username, ship_code ) " +
    		  "VALUES ( " +
    		  "'" + Esc.escape( user ) + "', " +
    		  "'" + Esc.escape( code ) + "' )";
    	transaction.add( sql );

    	connection.executeUpdate( "paxar_profile", transaction );
    }
}
