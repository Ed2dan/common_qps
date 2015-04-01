package com.paxar.qps.orderhistory;

import com.paxar.qps.data.ContactInfo;
import com.paxar.qps.data.OracleInfo;
import com.averydennison.data.AbstractDAO;

import java.sql.ResultSet;
import java.io.File;
import java.util.Vector;

public class OrderHistoryDAO extends AbstractDAO
{
    public static void saveOrder( String username, String retailer, File orderFile,
                                  String contractorPO, String serviceBureau, String searchable[] ) throws Exception
    {
        checkConnection( );

        String sql = "INSERT INTO order_history ( user_name, retailer, " +
                     "order_file, po, service_bureau, searchable ) " +
                     "VALUES ( " +
                     "'" + esc( username ) + "'," +
                     "'" + esc( retailer ) + "'," +
                     "'" + esc( orderFile.getName( )) + "', " +
                     "'" + esc( contractorPO ) + "', " +
                     "'" + esc( serviceBureau ) + "', ";
        if ( searchable.length == 0 )
        {
            sql += "'{}' )";
        } else
        {
            sql += " ARRAY[ '" + searchable[0] + "'";
            for ( int i = 1; i < searchable.length; i++ )
            {
                sql += ", '" + esc( searchable[i] ) + "'";
            }
            sql += " ] )";
        }
        connection.executeUpdate( "qps", sql );
    }

    public static void saveOrder( String retailer, File orderFile,
                                  ContactInfo info ) throws Exception
    {
        checkConnection( );

        String sql = "INSERT INTO order_history ( user_name, retailer, " +
                     "order_file, po, service_bureau, searchable ) " +
                     "VALUES ( " +
                     "'" + esc( info.username ) + "'," +
                     "'" + esc( retailer ) + "'," +
                     "'" + esc( orderFile.getName( )) + "', " +
                     "'" + esc( info.contractorPO ) + "', " +
                     "'" + esc( info.serviceBureau ) + "', '{}' )";
        connection.executeUpdate( "qps", sql );
    }

    public static void saveOrder( String retailer, File orderFile,
                                  ContactInfo info, String searchable[] ) throws Exception
    {
        checkConnection( );

        String sql = "INSERT INTO order_history ( user_name, retailer, " +
                     "order_file, po, service_bureau, searchable ) " +
                     "VALUES ( " +
                     "'" + esc( info.username ) + "'," +
                     "'" + esc( retailer ) + "'," +
                     "'" + esc( orderFile.getName( )) + "', " +
                     "'" + esc( info.contractorPO ) + "', " +
                     "'" + esc( info.serviceBureau ) + "', ";
        if ( searchable.length == 0 )
        {
            sql += "'{}' )";
        } else
        {
            sql += " ARRAY[ '" + searchable[0] + "'";
            for ( int i = 1; i < searchable.length; i++ )
            {
                sql += ", '" + esc( searchable[i] ) + "'";
            }
            sql += " ] )";
        }
        connection.executeUpdate( "qps", sql );
    }


    public static void saveOrder( String retailer, File orderFile,
                                  OracleInfo info, String searchable[] ) throws Exception
    {
        checkConnection( );

        String sql = "INSERT INTO order_history ( user_name, retailer, " +
                     "order_file, po, service_bureau, searchable ) " +
                     "VALUES ( " +
                     "'" + esc( info.getValue(OracleInfo.user_name) ) + "'," +
                     "'" + esc( retailer ) + "'," +
                     "'" + esc( orderFile.getName( )) + "', " +
                     "'" + esc( info.getValue(OracleInfo.contractor_po) ) + "', " +
                     "'" + esc( info.getValue(OracleInfo.service_bureau) ) + "', ";
        if ( searchable.length == 0 )
        {
            sql += "'{}' )";
        } else
        {
            sql += " ARRAY[ '" + searchable[0] + "'";
            for ( int i = 1; i < searchable.length; i++ )
            {
                sql += ", '" + esc( searchable[i] ) + "'";
            }
            sql += " ] )";
        }
        connection.executeUpdate( "qps", sql );
    }


    public static String getOrderDirectory( String serverBase, String retailer )
    {
        String dir = serverBase + File.separator + "orders" + File.separator +
               retailer.replaceAll( " ", "" );
        new File( dir ).mkdirs( );
        return dir;
    }

    public static String[] getUsers( ) throws Exception
    {
        Vector results = new Vector( );
        String sql = "SELECT DISTINCT order_history.user_name " +
                     "FROM order_history ORDER BY order_history.user_name";
        ResultSet rsUser = connection.executeQuery( "qps", sql );
        while ( rsUser.next( ))
        {
            results.add( rsUser.getString( 1 ));
        }
        String users[] = new String[results.size( )];
        results.copyInto( users );
        return users;
    }

    public static String[] getRetailers( ) throws Exception
    {
        Vector results = new Vector( );
        String sql = "SELECT DISTINCT order_history.retailer " +
                     "FROM order_history ORDER BY order_history.retailer";
        ResultSet rsRetailer = connection.executeQuery( "qps", sql );
        while ( rsRetailer.next( ))
        {
            results.add( rsRetailer.getString( 1 ));
        }
        String retailers[] = new String[results.size( )];
        results.copyInto( retailers );
        return retailers;
    }
    
    public static String[] getRetailers( String user ) throws Exception
    {
        String sql = "SELECT DISTINCT retailers.retailer FROM retailers WHERE " +
                     "retailers.user_name = '" + esc( user ) + "'";
        Vector results = new Vector( );
        ResultSet rs = connection.executeQuery( "paxar_profile", sql );
        while ( rs.next( ))
        {
            results.add( rs.getString( 1 ));
        }
        String retailers[] = new String[results.size( )];
        results.copyInto( retailers );
        return retailers;
    }
    
    public static String[] getUsers( String retailer, String user ) throws Exception
    {
        Vector results = new Vector( );
        String sql = "SELECT DISTINCT a.user_name " +
                     "FROM retailers a, retailers b " +
                     "WHERE a.retailer = b.retailer " +
                     "AND b.admin = 1 " +
                     "AND b.retailer = '" + esc( retailer ) + "' " +
                     "AND b.user_name = '" + esc( user ) + "'";
        ResultSet rsUser = connection.executeQuery( "paxar_profile", sql );
        while ( rsUser.next( ))
        {
            results.add( rsUser.getString( 1 ));
        }
        if ( ! results.contains( user ))
        {
            results.add( user );
        }
        String users[] = new String[results.size( )];
        results.copyInto( users );
        return users;
    }  
    
    public static String[] getUsers( String user ) throws Exception
    {
        Vector results = new Vector( );
        String sql = "SELECT DISTINCT a.user_name " +
                     "FROM retailers a, retailers b " +
                     "WHERE a.retailer = b.retailer " +
                     "AND b.admin = 1 " +
                     "AND b.user_name = '" + esc( user ) + "'";
        ResultSet rsUser = connection.executeQuery( "paxar_profile", sql );
        while ( rsUser.next( ))
        {
            results.add( rsUser.getString( 1 ));
        }
        if ( ! results.contains( user ))
        {
            results.add( user );
        }
        String users[] = new String[results.size( )];
        results.copyInto( users );
        return users;
    }    

    public static Order[] getCustomerOrderHistory( String admin, String user, String retailer,
                                                   String po, String fromDate, String toDate ) throws Exception
    {
        checkConnection( );
        
        String sql = Order.SQL_BASE + " WHERE ";
        sql += " order_history.retailer = '" + esc( retailer ) + "' ";
        if ( user == null || user.equalsIgnoreCase( "ALL" ))
        {
            String users[] = getUsers( retailer, admin );
            if ( users.length == 0 )
            {
                sql += " 1 = 0 ";
            } else
            {
                sql += " AND order_history.user_name IN ( '" + esc( users[0] ) + "'";
                for ( int i = 1; i < users.length; i++ )
                {
                    sql += ", '" + users[i] + "'";
                }
                sql += " ) ";
            }
        } else 
        {
            sql += " AND order_history.user_name = '" + esc( user ) + "' ";
        }
        sql += po == null || po.toUpperCase( ).equals( "ALL" ) ?
                " AND TRUE " :
                " AND ( order_history.po LIKE '" + esc( po ).replaceAll( "[*]", "%" ) + "' " +
                "       OR '" + esc( po ).replaceAll( "[*]", "" ) + "' = ANY( searchable )) ";
        sql += fromDate != null && fromDate.trim( ).length( ) > 0 ?
               " AND order_history.order_date >= '" + fromDate + " 00:00:00' " :
               " AND TRUE";
        sql += toDate != null && toDate.trim( ).length( ) > 0 ?
               " AND order_history.order_date <= '" + toDate + " 23:59:50' " : 
               " AND TRUE";
        return doOrderQuery( sql, 500 );
    }
    
    public static Order[] getOrderHistory( String user, String retailer,
                                           String po, long limit ) throws Exception
    {
        checkConnection( );

        String sql = Order.SQL_BASE + " WHERE ";
        sql += user == null || user.toUpperCase( ).equals( "ALL" ) ?
               " TRUE " :
               " order_history.user_name = '" + esc( user ) + "' ";
        sql += retailer == null || retailer.toUpperCase( ).equals( "ALL" ) ?
               " AND TRUE " :
               " AND order_history.retailer = '" + esc( retailer ) + "' ";
        sql += po == null || po.toUpperCase( ).equals( "ALL" ) ?
               " AND TRUE " :
               " AND ( order_history.po = '" + esc( po ) + "' " +
               "       OR '" + esc( po ) + "' = ANY( searchable )) " ;
        return doOrderQuery( sql, limit );
    }

    public static Order[] getOrderHistory( String user, String retailer,
                                           String po, String fromDate,
                                           String toDate ) throws Exception
    {
        checkConnection( );

        String sql = Order.SQL_BASE + " WHERE ";
        sql += user == null || user.toUpperCase( ).equals( "ALL" ) ?
               " TRUE " :
               " order_history.user_name = '" + esc( user ) + "' ";
        sql += retailer == null || retailer.toUpperCase( ).equals( "ALL" ) ?
               " AND TRUE " :
               " AND order_history.retailer = '" + esc( retailer ) + "' ";
        sql += po == null || po.toUpperCase( ).equals( "ALL" ) ?
               " AND TRUE " :
               " AND ( order_history.po = '" + esc( po ) + "' " +
               "       OR '" + esc( po ) + "' = ANY( searchable ))";
        sql += " AND order_history.order_date >= '" + fromDate + " 00:00:00' ";
        sql += " AND order_history.order_date <= '" + toDate + " 23:59:50' ";
        return doOrderQuery( sql, -1 );
    }

    public static Order[] getOrderHistory( String user, String retailer,
                                           String po ) throws Exception
    {
        return getOrderHistory( user, retailer, po, -1 );
    }

    protected static Order[] doOrderQuery( String sql, long limit ) throws Exception
    {
        checkConnection( );

        sql += " ORDER BY order_date DESC";
        sql += limit == -1 ? "" : " LIMIT " + limit;
        Vector results = new Vector( );
        ResultSet rsOrders = null;
        try
        {
            rsOrders = connection.executeQuery( "qps", sql );
            while ( rsOrders.next( ))
            {
                results.add( new Order( rsOrders ));
            }
        } finally
        {
            if ( rsOrders != null )
            {
                rsOrders.close( );
            }
        }

        Order orders[] = new Order[ results.size( )];
        results.copyInto( orders );
        return orders;
    }
}
