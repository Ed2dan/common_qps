package com.paxar.qps.data;

import java.util.Vector;
import java.sql.ResultSet;
import com.paxar.sql.Esc;

public class SiteBureauDAO extends AbstractDAO
{
    public static String[] getServiceBureaus( String username, String retailer ) throws Exception
    {
        checkConnection( );

        String sql = "";

        sql = "SELECT DISTINCT site_bureau.service_bureau " +
              "FROM site_bureau " +
              "INNER JOIN userinfo " +
              "ON site_bureau.site_name = userinfo.site " +
              "AND userinfo.user_name = '" + Esc.escape( username ) + "' " +
              "AND site_bureau.retailer = '" + Esc.escape( retailer ) + "' " +
              "INNER JOIN service_bureau " +
              "ON service_bureau.service_bureau = site_bureau.service_bureau " +
              "AND service_bureau.retailer = '" + Esc.escape( retailer ) + "'";

        Vector results = new Vector( );
        ResultSet rs = connection.executeQuery( "paxar_profile", sql );
        while ( rs.next( ))
        {
            results.add( rs.getString( 1 ));
        }
        rs.close( );

        String bureaus[] = new String[results.size( )];
        results.copyInto( bureaus );
        return bureaus;
    }
}
