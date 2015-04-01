package com.paxar.qps.data;
import java.util.*;
import java.sql.*;

/**
 * <p>Title: DivisionDAO</p>
 *
 * <p>Description: Get division name of SB</p>
 *
 * @author jerome
 * @version 1.0
 */
public class DivisionDAO extends AbstractDAO
{
    public DivisionDAO()
    {
    }

    public static String getDivision(String serviceBureau) throws Exception
    {
        String division = "";

        checkConnection();

        String sql = "SELECT division FROM division WHERE service_bureau='" + serviceBureau.trim() + "' LIMIT 1";
        ResultSet rs = connection.executeQuery( "qps", sql );
        if (rs.next())
            division = rs.getString("division");
        rs.close();
        return division;
    }

    public static String[] getDivisions() throws Exception
    {
        checkConnection();
        Vector vc = new Vector();
        String sql = "SELECT division FROM division ";
        sql += " ORDER BY division ";

        ResultSet rs = connection.executeQuery( "qps", sql );
        while (rs.next())
            vc.add(rs.getString("division"));
        rs.close();
        String result[] = new String[vc.size()];
        vc.copyInto(result);
        return result;
    }

    public static String[] getAdminDivision(String user, String retailer) throws Exception
    {
        checkConnection();
        Vector vc = new Vector();
        String sql = "SELECT division FROM division_admin ";
        sql += "WHERE user_name='" + user + "' ";
        sql += " AND (retailer='*' OR retailer='" + retailer + "') ";
        sql += " ORDER BY division ";

        ResultSet rs = connection.executeQuery( "qps", sql );
        while (rs.next())
            vc.add(rs.getString("division"));
        rs.close();
        if (vc.size()>0 && ((String)vc.get(0)).equals("*"))
            return getDivisions();
        String result[] = new String[vc.size()];
        vc.copyInto(result);
        return result;
    }
}
