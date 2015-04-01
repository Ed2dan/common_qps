package com.paxar.qps.data;
import java.util.Vector;
import java.sql.*;

/**
 *
 * <p>Title: FreightTermsDAO</p>
 *
 * <p>Description: A class to get data from qps/frieght_terms table</p>
 *
 * @author jerome
 * @version 1.0
 */

public class FreightTermsDAO extends AbstractDAO
{
    public FreightTermsDAO()
    {
    }

    public static String getFreightCode(String desc) throws Exception
    {
        checkConnection();
        String sql = "SELECT terms_code FROM freight_terms WHERE terms_desc='" + desc + "' ";
        ResultSet rs = connection.executeQuery( "qps", sql );
        String result = desc;
        if (rs.next())
        {
            result = rs.getString("terms_code");
        }
        rs.close();
        return result;
    }

    public static String getFreightDesc(String code) throws Exception
    {
        checkConnection();
        String sql = "SELECT terms_desc FROM freight_terms WHERE terms_code='" + code + "' ";
        ResultSet rs = connection.executeQuery( "qps", sql );
        String result = code;
        if (rs.next())
        {
            result = rs.getString("terms_desc");
        }
        rs.close();
        return result;
    }

    public static String[] getFreightTerms() throws Exception
    {
        checkConnection();
        String sql = "SELECT terms_desc FROM freight_terms ORDER BY terms_desc ";
        ResultSet rs = connection.executeQuery( "qps", sql );
        Vector vc = new Vector();
        while (rs.next())
        {
            vc.add(rs.getString("terms_desc"));
        }
        rs.close();
        String terms[] = new String[vc.size()];
        vc.copyInto(terms);
        return terms;
    }

}
