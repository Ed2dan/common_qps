package com.paxar.qps.data;

import java.sql.ResultSet;
import java.util.Vector;

/**
 * <p>Title: ForwardUserDAO</p>
 *
 * <p>Description: Handles data access to get list of available forward users</p>
 *
 * @author jerome
 * @version 1.0
 */
public class ForwardUserDAO extends AbstractDAO
{
    public ForwardUserDAO()
    {
    }

    public static String[] getForwardUser(String user) throws Exception
    {
        return getForwardUser(user, null);
    }
    public static String[] getForwardSite(String user) throws Exception
    {
        return getForwardSite(user, null);
    }

    public static String[] getForwardUser(String user, String retailer) throws Exception
    {
        checkConnection();
        Vector vc = new Vector();
        String sql = "SELECT DISTINCT forward.forward_user, upper(forward.forward_user) FROM forward ";
        if (retailer != null)
            sql += "JOIN retailers ON forward.forward_user=retailers.user_name ";
        sql += "WHERE forward.user_name='" + user + "' ";
        sql += "AND forward.site is NULL ";
        if (retailer != null)
            sql += " AND upper(retailers.retailer)='" + retailer.toUpperCase() + "' ";
        sql += "ORDER BY upper(forward.forward_user)";
        ResultSet rs = connection.executeQuery( "paxar_profile", sql );
        while ( rs.next() )
            vc.add( rs.getString( "forward_user" ) );
        rs.close();

        if ( vc.size() == 0 )
            return null;
        String result[] = new String[vc.size() ];
        vc.copyInto( result );
        return result;
    }

    public static String[] getForwardSite(String user, String retailer) throws Exception
    {
        checkConnection();
        Vector vc = new Vector();
        String sql = "SELECT DISTINCT forward.site, upper(forward.site) FROM forward ";
        if (retailer != null)
            sql += "JOIN sites ON forward.site=sites.site_name ";
        sql += "WHERE forward.user_name='" + user + "' ";
        if (retailer != null)
            sql += " AND upper(sites.retailer)='" + retailer.toUpperCase() + "' ";
        sql += " AND forward.forward_user is NULL ORDER BY upper(forward.site)";
        ResultSet rs = connection.executeQuery( "paxar_profile", sql );
        while ( rs.next() )
            vc.add( rs.getString( "site" ) );
        rs.close();

        if ( vc.size() == 0 )
            return null;
        String result[] = new String[vc.size() ];
        vc.copyInto( result );
        return result;
    }

}
