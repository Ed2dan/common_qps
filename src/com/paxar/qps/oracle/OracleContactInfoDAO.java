package com.paxar.qps.oracle;

import java.util.Vector;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.paxar.qps.data.ContactInfoDAO;


/**
 * Get contact info data from Oracle
 *
 * @author jerome
 */


public class OracleContactInfoDAO extends com.paxar.qps.data.ContactInfoDAO
{
    public OracleContactInfoDAO()
    {
    }

    /**
     * Retrieve bill to information from Oracle according to billNumber
     *
     * @param info OracleContactInfo result saves here
     * @param billNumber String
     * @throws SQLException
     */
    public static void fillBillTo( OracleContactInfo info, String billNumber ) throws
        SQLException
    {
        String customerName[] = getCustomer( billNumber );
        if ( customerName != null )
        {
            info.billCompany = customerName[ 1 ];
            info.billCustomerNo = customerName[ 0 ];
            info.billAddressID = getAddressID( customerName[ 2 ], "BILL_TO" );
        }
        else
        {
            info.billCompany = "";
            info.billCustomerNo = "";
            info.billAddressID = "";
        }

        // Get Address
        String contactAddress[] = getContactAddress( info.billAddressID );
        if ( customerName != null && contactAddress != null )
        {
            info.billAddress1 = contactAddress[ 1 ];
            info.billAddress2 = contactAddress[ 2 ];
            info.billAddress3 = contactAddress[ 3 ];
            info.billAddress4 = contactAddress[ 4 ];
            info.billCity = contactAddress[ 5 ];
            info.billState = contactAddress[ 6 ];
            info.billZip = contactAddress[ 7 ];
            info.billCountryCode = contactAddress[ 8 ];
        }
        else
        {
            info.billAddress1 = "";
            info.billAddress2 = "";
            info.billAddress3 = "";
            info.billAddress4 = "";
            info.billCity = "";
            info.billState = "";
            info.billZip = "";
            info.billCountryCode = "";
        }

        // Get Contact
        String contactPerson[] = getContactPerson( info.billAddressID );
        if ( customerName != null && contactPerson != null )
        {
            info.billContact = contactPerson[ 0 ];
            info.billPhone = contactPerson[ 1 ];
            info.billFax = contactPerson[ 2 ];
        }
        else
        {
            info.billContact = "";
            info.billPhone = "";
            info.billFax = "";
        }
    }



    /**
     * Retrieve ship to information from Oracle according to shipNumber
     *
     * @param info OracleContactInfo result saves here
     * @param shipNumber String
     * @throws SQLException
     */
    public static void fillShipTo( OracleContactInfo info, String shipNumber ) throws
        SQLException
    {
        String contactName[] = getCustomer( shipNumber );
        if ( contactName != null )
        {
            info.shipCompany = contactName[ 1 ];
            info.shipCustomerNo = contactName[ 0 ];
            info.shipAddressID = getAddressID( contactName[ 2 ], "SHIP_TO" );
        }
        else
        {
            info.shipCompany = "";
            info.shipCustomerNo = "";
            info.shipAddressID = "";
        }

        // Get Address
        String contactAddress[] = getContactAddress( info.shipAddressID );
        if ( contactName != null && contactAddress != null )
        {
            info.shipAddress1 = contactAddress[ 1 ];
            info.shipAddress2 = contactAddress[ 2 ];
            info.shipAddress3 = contactAddress[ 3 ];
            info.shipAddress4 = contactAddress[ 4 ];
            info.shipCity = contactAddress[ 5 ];
            info.shipState = contactAddress[ 6 ];
            info.shipZip = contactAddress[ 7 ];
            info.shipCountryCode = contactAddress[ 8 ];
        }
        else
        {
            info.shipAddress1 = "";
            info.shipAddress2 = "";
            info.shipAddress3 = "";
            info.shipAddress4 = "";
            info.shipCity = "";
            info.shipState = "";
            info.shipZip = "";
            info.shipCountryCode = "";
        }

        // Get Contact
        String contactPerson[] = getContactPerson( info.shipAddressID );
        if ( contactName != null && contactPerson != null )
        {
            info.shipContact = contactPerson[ 0 ];
            info.shipPhone = contactPerson[ 1 ];
            info.shipFax = contactPerson[ 2 ];
        }
        else
        {
            info.shipContact = "";
            info.shipPhone = "";
            info.shipFax = "";
        }
    }

    /**
     * Get Oracle Address ID of the Customer ID
     *
     * @param customerID String
     * @param relation String BILL_TO or SHIP_TO
     * @return String
     * @throws SQLException
     */
    public static String getAddressID( String customerID, String relation ) throws
        SQLException
    {
        try
        {
            long a = Long.parseLong( customerID );
        }
        catch ( Exception e )
        {
            return null;
        }
        String addressID = null;
        OracleConnection conn = null;
        try
        {
            conn = new OracleConnection();
            String sql = "SELECT max(B.ADDRESS_ID) ADDRESS ";
            sql += "FROM APPS.RA_ADDRESSES_ALL B, APPS.RA_SITE_USES_ALL C ";
            sql += "WHERE B.CUSTOMER_ID=" + customerID + " ";
            sql += " AND B.ADDRESS_ID=C.ADDRESS_ID ";
            sql += " AND B.STATUS='A' ";
            sql += " AND C.SITE_USE_CODE='" + relation + "' ";
            ResultSet rs = conn.executeQuery( sql );
            if ( rs.next() )
            {
                addressID = rs.getString( "ADDRESS" );
            }
            rs.close();
        }
        finally
        {
            if ( conn != null )conn.close();
        }
        return addressID;
    }

    /**
     * Get Oracle customer information by customer number
     *
     * @param customerNumber String
     * @return String[]
     * @throws SQLException
     */
    public static String[] getCustomer( String customerNumber ) throws
        SQLException
    {
        try
        {
            long a = Long.parseLong( customerNumber );
        }
        catch ( Exception e )
        {
            return null;
        }
        String customer[] = null;
        OracleConnection conn = null;
        try
        {
            conn = new OracleConnection();
            String sql =
                "SELECT A.CUSTOMER_NAME, A.CUSTOMER_ID, A.CUSTOMER_NUMBER ";
            sql += "FROM APPS.RA_CUSTOMERS A ";
            sql += "WHERE A.CUSTOMER_NUMBER=" + customerNumber + " ";
            sql += "ORDER BY A.CUSTOMER_NAME ";
            ResultSet rs = conn.executeQuery( sql );
            if ( rs.next() )
            {
                customer = new String[3 ];
                customer[ 0 ] = rs.getString( "CUSTOMER_NUMBER" );
                customer[ 1 ] = rs.getString( "CUSTOMER_NAME" );
                customer[ 2 ] = rs.getString( "CUSTOMER_ID" );

            }
            rs.close();
        }
        finally
        {
            if ( conn != null )conn.close();
        }
        return customer;
    }

    /**
     * Get Oracle customer information by siteNumber
     *
     * @param siteNumber String
     * @return String[]
     * @throws SQLException
     */
    public static String[] getCustomerBySiteNo( String siteNumber ) throws
        SQLException
    {
        try
        {
            long a = Long.parseLong( siteNumber );
        }
        catch ( Exception e )
        {
            return null;
        }
        String customer[] = null;
        OracleConnection conn = null;
        try
        {
            conn = new OracleConnection();
            String sql = "SELECT C.SITE_USE_CODE, A.CUSTOMER_NAME, B.SITE_NUMBER, B.ADDRESS1, B.ADDRESS2";
            sql += ", B.ADDRESS3, B.ADDRESS4, B.CITY, B.STATE, B.POSTAL_CODE, B.COUNTRY, A.CUSTOMER_NUMBER, B.ADDRESS_ID ";
            sql += "FROM APPS.RA_CUSTOMERS A, APPS.RA_ADDRESSES_ALL B, APPS.RA_SITE_USES_ALL C ";
            sql += "WHERE A.CUSTOMER_ID = B.CUSTOMER_ID ";
            sql += "AND B.ADDRESS_ID = C.ADDRESS_ID ";
            sql += "AND B.ORG_ID = 1038 ";
            sql += "AND C.SITE_USE_CODE in ('BILL_TO','SHIP_TO') ";
            sql += "AND B.STATUS = 'A' ";
            sql += "AND B.SITE_NUMBER = '" + siteNumber + "' ";
            sql += "ORDER BY 1";

            ResultSet rs = conn.executeQuery( sql );
            if ( rs.next() )
            {
                customer = new String[13];
                customer[ 0 ] = rs.getString( "SITE_USE_CODE" );
                customer[ 1 ] = rs.getString( "CUSTOMER_NAME" );
                customer[ 2 ] = rs.getString( "SITE_NUMBER" );
                customer[ 3 ] = rs.getString( "ADDRESS1" );
                customer[ 4 ] = rs.getString( "ADDRESS2" );
                customer[ 5 ] = rs.getString( "ADDRESS3" );
                customer[ 6 ] = rs.getString( "ADDRESS4" );
                customer[ 7 ] = rs.getString( "CITY" );
                customer[ 8 ] = rs.getString( "STATE" );
                customer[ 9 ] = rs.getString( "POSTAL_CODE" );
                customer[ 10 ] = rs.getString( "COUNTRY" );
                customer[ 11 ] = rs.getString( "CUSTOMER_NUMBER" );
                customer[ 12 ] = rs.getString( "ADDRESS_ID" );
            }
            rs.close();
        }
        finally
        {
            if ( conn != null )conn.close();
        }
        return customer;
    }


    /**
     * Get Oracle contact address
     *
     * @param addressID String
     * @return String[]
     * @throws SQLException
     */
    public static String[] getContactAddress( String addressID ) throws
        SQLException
    {
        try
        {
            long a = Long.parseLong( addressID );
        }
        catch ( Exception e )
        {
            return null;
        }

        String dest[] = null;
        OracleConnection conn = null;
        try
        {
            conn = new OracleConnection();
            String sql =
                "SELECT DISTINCT SITE_NUMBER, ADDRESS1, ADDRESS2, ADDRESS3, ADDRESS4, ";
            sql += " CITY, STATE, POSTAL_CODE, COUNTRY, ADDRESS_ID ";
            sql += " FROM APPS.RA_ADDRESSES_ALL ";
            sql += " WHERE STATUS = 'A' ";
            sql += " AND ADDRESS_ID = " + addressID + " ";
            sql += " ORDER BY SITE_NUMBER, ADDRESS1, ADDRESS2, ADDRESS3 ";

            ResultSet rs = conn.executeQuery( sql );
            if ( rs.next() )
            {
                dest = new String[10 ];
                dest[ 0 ] = rs.getString( "SITE_NUMBER" );
                dest[ 1 ] = rs.getString( "ADDRESS1" );
                dest[ 2 ] = rs.getString( "ADDRESS2" );
                dest[ 3 ] = rs.getString( "ADDRESS3" );
                dest[ 4 ] = rs.getString( "ADDRESS4" );
                dest[ 5 ] = rs.getString( "CITY" );
                dest[ 6 ] = rs.getString( "STATE" );
                dest[ 7 ] = rs.getString( "POSTAL_CODE" );
                dest[ 8 ] = rs.getString( "COUNTRY" );
                dest[ 9 ] = rs.getString( "ADDRESS_ID" );
                for ( int i = 0; i < 10; i++ )
                    if ( dest[ i ] == null )
                        dest[ i ] = "";
            }
            rs.close();
        }
        finally
        {
            if ( conn != null )conn.close();
        }
        return dest;
    }

    /**
     * Get Contact Person
     *
     * @param addressID String
     * @return String[]
     * @throws SQLException
     */

    public static String[] getContactPerson( String addressID ) throws
        SQLException
    {
        try
        {
            long a = Long.parseLong( addressID );
        }
        catch ( Exception e )
        {
            return null;
        }

        String contact[] = new String[3 ];
        Vector contactPerson = new Vector();
        Vector contactPhone = new Vector();
        Vector contactFax = new Vector();

        OracleConnection conn = null;
        try
        {
            conn = new OracleConnection();

            String sql = " SELECT RC.ADDRESS_ID,RC.LAST_NAME,RC.FIRST_NAME,HCP.PHONE_line_TYPE,HCP.PHONE_NUMBER ";
            sql +=
                " FROM APPS.RA_CONTACTS RC, APPS.RA_PHONES RP, APPS.HZ_CONTACT_POINTS HCP ";
            sql += " WHERE RC.CONTACT_ID = RP.CONTACT_ID(+) AND RP.PHONE_ID = HCP.CONTACT_POINT_ID(+) ";
            sql += " AND RC.ADDRESS_ID = " + addressID + " ";
            sql += " ORDER BY RC.LAST_NAME, RC.FIRST_NAME, HCP.PHONE_NUMBER";
            ResultSet rs = conn.executeQuery( sql );

            while ( rs.next() )
            {
                String FirstName = rs.getString( "FIRST_NAME" );
                String LastName = rs.getString( "LAST_NAME" );
                String Phone = rs.getString( "PHONE_NUMBER" );
                String PhoneType = rs.getString( "PHONE_LINE_TYPE" );

                if ( FirstName == null )
                    FirstName = "";
                if ( LastName == null )
                    LastName = "";
                if ( Phone == null )
                    Phone = "";
                else
                    Phone = Phone.trim();
                if ( PhoneType == null )
                    PhoneType = "";
                else
                    PhoneType = PhoneType.trim().toUpperCase();
                String FullName = ( FirstName + " " + LastName ).trim();

                if ( FullName.length() > 0 && !contactPerson.contains( FullName ) )
                    contactPerson.add( FullName );

                if ( Phone.length() > 0 )
                {
                    if ( PhoneType.equals( "FAX" ) ||
                         PhoneType.equals( "FACSIMILE" ) )
                    {
                        if ( !contactFax.contains( Phone ) )
                            contactFax.add( Phone );
                    }
                    else
                    if ( !contactPhone.contains( Phone ) )
                        contactPhone.add( Phone );
                }
            }

            contact[ 0 ] = vectorToString( contactPerson );
            contact[ 1 ] = vectorToString( contactPhone );
            contact[ 2 ] = vectorToString( contactFax );

            rs.close();
        }
        finally
        {
            if ( conn != null )conn.close();
        }

        return contact;
    }

    /**
     * Turn Strings inside a Vector to a/b/c
     *
     * @param vc Vector
     * @return String
     */
    protected static String vectorToString( Vector vc )
    {
        String dest = "";
        String delimiter = "/";
        for ( int i = 0; i < vc.size() && i < 3; i++ )
        {
            if ( i > 0 )
                dest += delimiter;
            dest += ( String )vc.get( i );
        }
        return dest;
    }

    public static void updateShipping( OracleContactInfo info ) throws
        Exception
    {
        ContactInfoDAO.updateShipping( info );
    }

    public static void updateBilling( OracleContactInfo info ) throws Exception
    {
        ContactInfoDAO.updateBilling( info );
    }

    public static void updateEmail( OracleContactInfo info ) throws Exception
    {
        ContactInfoDAO.updateEmail( info );
    }

    /**
     * Get default bill ship info by username
     * @param username String
     * @return OracleContactInfo
     * @throws Exception
     */
    public static OracleContactInfo getOracleInfo( String username ) throws
        Exception
    {
        OracleContactInfo info = new OracleContactInfo( username );
        info.setContactInfo( ContactInfoDAO.getInfo( username ) );
        if (info.billCountryCode==null)
            info.billCountryCode = "";
        if (info.shipCountryCode == null)
            info.shipCountryCode = "";

        if (info.shipTerm == null)
            info.shipTerm = info.getDefaultShipTerm();

        if ( info.billCountryCode.length() > 4 )
            info.billCountryCode.substring( 0, 4 );
        if ( info.shipCountryCode.length() > 4 )
            info.shipCountryCode.substring( 0, 4 );

        info.shipDate = OracleContactInfo.getDefaultShipDate();
        info.shipPromiseDate = info.shipDate;
        return info;
    }

    /**
     * This is the new ship to lookup replaces the aboves
     * @param customerID String
     * @return OracleContactInfo[]
     * @throws Exception
     */
    public static Vector listOracleInfo( long customerID, long orgID ) throws
        Exception
    {
        OracleConnection conn = null;
        Vector vc = new Vector();
        try
        {
            conn = new OracleConnection();
            String sql = "SELECT ";
            sql += " A.CUSTOMER_NAME, ";
            sql += " B.SITE_NUMBER, B.ORG_ID, B.ADDRESS_ID, ";
            sql += " B.ADDRESS1, B.ADDRESS2, B.ADDRESS3, B.ADDRESS4, ";
            sql += " B.CITY, B.STATE, B.POSTAL_CODE, B.COUNTRY ";
            sql += " FROM APPS.RA_CUSTOMERS A, APPS.RA_ADDRESSES_ALL B ";
            sql += " WHERE A.CUSTOMER_ID = B.CUSTOMER_ID ";
            if ( customerID > 0 )
                sql += " AND A.CUSTOMER_NUMBER=" + customerID + " ";
            if ( orgID > 0 )
                sql += " AND B.ORG_ID=" + orgID + " ";
            sql += " ORDER BY SITE_NUMBER ";

            if ( customerID + orgID == 0 )
                throw new Exception( "Oracle SQL Argument not supplied" );

            ResultSet rs = conn.executeQuery( sql );
            while ( rs.next() )
            {
                HashMap map = new HashMap();
                try
                {
                    for ( int i = 1; i <= rs.getMetaData().getColumnCount(); i++ )
                    {
                        String value = rs.getString( i );
                        if ( value == null )
                            value = "";
                        map.put( rs.getMetaData().getColumnName( i ),
                                 value );
                    }
                    vc.add( map );
                }
                catch ( SQLException e )
                {
                    System.out.println( e );
                }
            }
        }
        finally
        {
            if ( conn != null )conn.close();
        }
        return vc;
    }

    /**
     * List Contact Persons
     * @param addressID long
     * @return Vector
     * @throws Exception
     */
    public static OracleContactPersons listContactInfo( long addressID ) throws Exception
    {
        OracleConnection conn = null;
        OracleContactPersons contact = new OracleContactPersons();
        try
        {
            conn = new OracleConnection();
            String sql = " SELECT RC.ADDRESS_ID,RC.LAST_NAME,RC.FIRST_NAME,HCP.PHONE_line_TYPE,HCP.PHONE_NUMBER ";
            sql +=
                " FROM APPS.RA_CONTACTS RC, APPS.RA_PHONES RP, APPS.HZ_CONTACT_POINTS HCP ";
            sql += " WHERE RC.CONTACT_ID = RP.CONTACT_ID(+) AND RP.PHONE_ID = HCP.CONTACT_POINT_ID(+) ";
            sql += " AND RC.ADDRESS_ID = " + addressID + " ";
            sql += " ORDER BY RC.LAST_NAME, RC.FIRST_NAME, HCP.PHONE_NUMBER";
            ResultSet rs = conn.executeQuery( sql );

            while ( rs.next() )
            {
                String FirstName = rs.getString( "FIRST_NAME" );
                String LastName = rs.getString( "LAST_NAME" );
                String Phone = rs.getString( "PHONE_NUMBER" );
                String PhoneType = rs.getString( "PHONE_LINE_TYPE" );
                if ( FirstName == null )
                    FirstName = "";
                if ( LastName == null )
                    LastName = "";
                if ( Phone == null )
                    Phone = "";
                else
                    Phone = Phone.trim();
                if ( PhoneType == null )
                    PhoneType = "";
                else
                    PhoneType = PhoneType.trim().toUpperCase();
                String FullName = ( LastName.trim() + " " + FirstName.trim() ).trim();
                contact.addContact(FullName, Phone, PhoneType);
            }
            rs.close();
        }
        finally
        {
            if ( conn != null )conn.close();
        }

        return contact;
    }
}




