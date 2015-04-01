package com.paxar.qps.oracle;
import java.sql.*;
import java.util.Vector;
import javax.servlet.http.HttpSession;

/**
 * @author Jerome
 *
 * OracleCountry provides data access of OracleCountry.
 *
 */

public class OracleCountryDAO
{
    public OracleCountryDAO()
    {
    }

    /**
     * Get a list of country from Oracle
     * @return OracleCountry[]
     * @throws Exception
     */
    public static OracleCountry[] getOracleCountry() throws Exception
    {
        Vector vc = new Vector();
        OracleConnection conn = null;
        try {
            conn = new OracleConnection();
            ResultSet rs = conn.executeQuery(
                "select DISTINCT TERRITORY_CODE country_code,TERRITORY_SHORT_NAME country_desc " +
                "from apps.fnd_territories_tl where source_lang='US' order by country_desc" );

            while ( rs.next() )
            {
                OracleCountry country = new OracleCountry( rs.getString(
                    "country_code" ), rs.getString( "country_desc" ) );
                if ( !country.getCountryDesc().startsWith( "Obsolete" ) )
                    vc.add( country );
            }
            rs.close();
        } finally { if (conn!=null) conn.close();}
        OracleCountry countries[] = new OracleCountry[vc.size()];
        vc.copyInto(countries);
        return countries;
    }

    public static OracleCountry[] getOracleCountry(HttpSession session) throws Exception
    {
        OracleCountry[] country = (OracleCountry[]) session.getAttribute("oracle_country");
        if (country==null)
        {
            country = getOracleCountry();
            session.setAttribute("oracle_country", country);
        }
        return country;
    }

    public static OracleCountry getOracleCountry(OracleCountry countries[], String countryDesc)
    {
        if (countryDesc==null || countries==null)
            return null;
        for (int i=0; i< countries.length; i++)
        {
            if (countries[i].getCountryDesc().toUpperCase().equals(countryDesc.trim().toUpperCase()))
                return countries[i];
        }
        return null;
    }

    public static boolean isValidCountry(OracleCountry countries[], String countryDesc)
    {
        if (getOracleCountry(countries, countryDesc)!=null)
            return true;
        return false;
    }
}
