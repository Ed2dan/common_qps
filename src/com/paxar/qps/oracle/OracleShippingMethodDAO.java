package com.paxar.qps.oracle;

import java.util.Vector;
import java.sql.ResultSet;
import javax.servlet.http.HttpSession;

/**
 * @author Jerome
 *
 * OracleShippingMethodDAO provides data access of OracleShippingMethod.
 *
 */

public class OracleShippingMethodDAO
{
    public OracleShippingMethodDAO()
    {
    }

    /**
     * Get shipping methods from Oracle
     *
     * @return String[] Shipping Method List
     * @throws Exception
     */
    public static String[] getOracleShippingMethod() throws Exception
    {
        Vector vc = new Vector();
        OracleConnection conn = null;
        try
        {
            conn = new OracleConnection();
            String sql =
                "SELECT DISTINCT wcs.ship_method_meaning shippingmethod ";
            sql +=
                " FROM   apps.wsh_carrier_services wcs, apps.wsh_org_carrier_services wocs ";
            sql += "  WHERE  UPPER(wocs.enabled_flag)= 'Y' ";
            sql += " AND    wocs.carrier_service_id = wcs.carrier_service_id ";
            sql += " ORDER BY shippingmethod ";
            ResultSet rs = conn.executeQuery( sql );

            while ( rs.next() )
            {
                vc.add( rs.getString( "shippingmethod" ) );
            }
            rs.close();
        }
        finally
        {
            if (conn!=null)  conn.close();
        }
        String ship[] = new String[vc.size() ];
        vc.copyInto( ship );
        return ship;
    }

    public static String[] getOracleShippingMethod(HttpSession session) throws Exception
    {
        String[] methods = ( String[] )session.getAttribute(
            "oracle_shipping_method" );
        if ( methods == null )
        {
            methods = getOracleShippingMethod();
            session.setAttribute( "oracle_shipping_method", methods );
        }
        return methods;
    }

}


