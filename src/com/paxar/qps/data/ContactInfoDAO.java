/*
 * $RCSfile: ContactInfoDAO.java,v $
 * $Revision: 1.7 $
 * $Date: 2013/08/16 04:04:46 $
 *
 * Copyright 2005 Paxar, Inc. All rights reserved.
 */
package com.paxar.qps.data;

import com.paxar.utilities.Esc;
import java.sql.ResultSet;
import java.util.Vector;

/**
 * The class <code>ContactInfoDAO</code> provides data
 * access functions to contact information.
 *
 * @author  Ty Busby
 */
public class ContactInfoDAO extends AbstractDAO
{
    /**
     * Updates the shipping information for a specific
     * user.
     *
     * @param info ContactInfo
     * @throws Exception
     */
    public static void updateShipping( ContactInfo info ) throws Exception
    {
        checkConnection( );

        Vector transaction = new Vector( 2 );

        String sql = "DELETE FROM ship_to WHERE user_name = '" + info.username + "'";
        transaction.add( sql );
        sql = "INSERT INTO ship_to ( user_name, contact, company_name, " +
              "address1, address2, address3, address4, city, " +
              "state, zip, country, phone, fax, via, customer_number, freight_term, country_code ) VALUES ( " +
              "'" + Esc.esc( info.username ) + "', " +
              "'" + Esc.esc( info.shipContact ) + "', " +
              "'" + Esc.esc( info.shipCompany ) + "', " +
              "'" + Esc.esc( info.shipAddress1 ) + "', " +
              "'" + Esc.esc( info.shipAddress2 ) + "', " +
              "'" + Esc.esc( info.shipAddress3 ) + "', " +
              "'" + Esc.esc( info.shipAddress4 ) + "', " +
              "'" + Esc.esc( info.shipCity ) + "', " +
              "'" + Esc.esc( info.shipState ) + "', " +
              "'" + Esc.esc( info.shipZip ) + "', " +
              "'" + Esc.esc( info.shipCountry ) + "', " +
              "'" + Esc.esc( info.shipPhone ) + "', " +
              "'" + Esc.esc( info.shipFax ) + "', " +
              "'" + Esc.esc( info.shipVia ) + "', " +
              "'" + Esc.esc( info.shipCustomerNo ) + "', " +
              "'" + Esc.esc( info.shipTerm ) + "', " +
              "'" + Esc.esc(info.shipCountryCode) + "' )";

        transaction.add( sql );
        connection.executeUpdate( "qps", transaction );
    }

    /**
     * Updates the billing information for a specific user.
     *
     * @param info ContactInfo
     * @throws Exception
     */
    public static void updateBilling( ContactInfo info ) throws Exception
    {
        checkConnection( );

        Vector transaction = new Vector( 2 );

        String sql = "DELETE FROM bill_to WHERE user_name = '" + info.username + "'";
        transaction.add( sql );
        sql = "INSERT INTO bill_to ( user_name, contact, company_name, " +
              "address1, address2, address3, address4, city, " +
              "state, zip, country, phone, fax, customer_number, country_code ) VALUES ( " +
              "'" + Esc.esc( info.username ) + "', " +
              "'" + Esc.esc( info.billContact ) + "', " +
              "'" + Esc.esc( info.billCompany ) + "', " +
              "'" + Esc.esc( info.billAddress1 ) + "', " +
              "'" + Esc.esc( info.billAddress2 ) + "', " +
              "'" + Esc.esc( info.billAddress3 ) + "', " +
              "'" + Esc.esc( info.billAddress4 ) + "', " +
              "'" + Esc.esc( info.billCity ) + "', " +
              "'" + Esc.esc( info.billState ) + "', " +
              "'" + Esc.esc( info.billZip ) + "', " +
              "'" + Esc.esc( info.billCountry ) + "', " +
              "'" + Esc.esc( info.billPhone ) + "', " +
              "'" + Esc.esc( info.billFax ) + "', " +
              "'" + Esc.esc( info.billCustomerNo) + "', " +
              "'" + Esc.esc( info.billCountryCode) + "' )";

        transaction.add( sql );
        connection.executeUpdate( "qps", transaction );
    }

    /**
     * Updates the contractor email address for a specific user.
     *
     * @param info ContactInfo
     * @throws Exception
     */
    public static void updateEmail( ContactInfo info ) throws Exception
    {
        checkConnection( );

        Vector transaction = new Vector( 2 );
        String sql = "DELETE FROM contractor_email " +
                     "WHERE user_name = '" + info.username + "'";
        transaction.add( sql );
        sql = "INSERT INTO contractor_email ( user_name, email ) " +
              "VALUES ( '" + info.username + "', '" + info.contractorEmail + "' )";
        transaction.add( sql );

        connection.executeUpdate( "qps", transaction );
    }

    /**
     * Loads a {@link ContactInfo} object with data from the
     * database for the specified user.
     *
     * @param username String
     * @return ContactInfo
     * @throws Exception
     */
    public static ContactInfo getInfo( String username ) throws Exception
    {
        checkConnection( );

        ContactInfo info = new ContactInfo( username );
        String sql = "SELECT contact, company_name, " +
                     "address1, address2, address3, address4, " +
                     "city, state, zip, country, phone, fax, via, customer_number, freight_term, country_code " +
                     "FROM ship_to WHERE user_name = '" + username + "'";
        ResultSet rsInfo = connection.executeQuery( "qps", sql );
        if ( rsInfo.next( ))
        {
            info.shipContact = rsInfo.getString( 1 );
            info.shipCompany = rsInfo.getString( 2 );
            info.shipAddress1 = rsInfo.getString( 3 );
            info.shipAddress2 = rsInfo.getString( 4 );
            info.shipAddress3 = rsInfo.getString( 5 );
            info.shipAddress4 = rsInfo.getString( 6 );
            info.shipCity = rsInfo.getString( 7 );
            info.shipState = rsInfo.getString( 8 );
            info.shipZip = rsInfo.getString( 9 );
            info.shipCountry = rsInfo.getString( 10 );
            info.shipPhone = rsInfo.getString( 11 );
            info.shipFax = rsInfo.getString( 12 );
            info.shipVia = rsInfo.getString( 13 );
            info.shipCustomerNo = rsInfo.getString(14);
            info.shipTerm = rsInfo.getString(15);
            info.shipCountryCode = rsInfo.getString(16);
        }
        rsInfo.close( );

        sql = "SELECT contact, company_name, " +
              "address1, address2, address3, address4, " +
              "city, state, zip, country, phone, fax, customer_number, country_code " +
              "FROM bill_to WHERE user_name = '" + username + "'";
        rsInfo = connection.executeQuery( "qps", sql );
        if ( rsInfo.next( ))
        {
            info.billContact = rsInfo.getString( 1 );
            info.billCompany = rsInfo.getString( 2 );
            info.billAddress1 = rsInfo.getString( 3 );
            info.billAddress2 = rsInfo.getString( 4 );
            info.billAddress3 = rsInfo.getString( 5 );
            info.billAddress4 = rsInfo.getString( 6 );
            info.billCity = rsInfo.getString( 7 );
            info.billState = rsInfo.getString( 8 );
            info.billZip = rsInfo.getString( 9 );
            info.billCountry = rsInfo.getString( 10 );
            info.billPhone = rsInfo.getString( 11 );
            info.billFax = rsInfo.getString( 12 );
            info.billCustomerNo = rsInfo.getString(13);
            info.billCountryCode = rsInfo.getString(14);
        }
        rsInfo.close( );

        sql = "SELECT email FROM contractor_email WHERE " +
              "user_name = '" + username + "' LIMIT 1";
        rsInfo = connection.executeQuery( "qps", sql );
        if ( rsInfo.next( ))
        {
            info.contractorEmail = rsInfo.getString( 1 );
        }
        rsInfo.close( );
        return info;
    }

    public static ContactInfo getInfoByRetailer(String retailer, String username) throws Exception
    {
        checkConnection();
        ContactInfo info = new ContactInfo( username );
        String sql = "SELECT * FROM user_billing ";
        sql += "WHERE user_name='" + username + "' AND retailer='" + retailer + "' ";
        sql += "ORDER BY id DESC LIMIT 1 ";
        ResultSet rs = connection.executeQuery("paxar_profile", sql);
        if (rs.next())
        {
        	info.billToRefNo = rs.getString("bill_to_ref_number");
            info.billCompany = rs.getString("bill_to_cust_name");
            info.billAddress1 = rs.getString("bill_to_address1");
            info.billAddress2 = rs.getString("bill_to_address2");
            info.billAddress3 = rs.getString("bill_to_address3");
            info.billCity = rs.getString("bill_to_city");
            info.billState = rs.getString("bill_to_state");
            info.billZip = rs.getString("bill_to_zip");
            info.billCountry = rs.getString("bill_to_country");
            info.billCountryCode = rs.getString("bill_to_country_code");
            info.billCustomerNo = rs.getString("bill_to_cust_number");
            
            info.soldToRefNo = rs.getString("ship_to_ref_number");
            info.soldToCustomerNo = rs.getString("sold_to_cust_number");
            info.soldCompany = rs.getString("sold_to_cust_name");
            info.soldAddress1 = rs.getString("sold_to_address1");
            info.soldAddress2 = rs.getString("sold_to_address2");
            info.soldAddress3 = rs.getString("sold_to_address3");
            info.soldCity = rs.getString("sold_to_city");
            info.soldState = rs.getString("sold_to_state");
            info.soldZip = rs.getString("sold_to_zip");
            info.soldCountry = rs.getString("sold_to_country");
            info.soldCountryCode = rs.getString("sold_to_country_code");
            
            info.shipToRefNo = rs.getString("ship_to_ref_number");
            info.shipCompany = rs.getString("ship_to_name");
            info.shipAddress1 = rs.getString("ship_to_address1");
            info.shipAddress2 = rs.getString("ship_to_address2");
            info.shipAddress3 = rs.getString("ship_to_address3");
            info.shipCity = rs.getString("ship_to_city");
            info.shipState = rs.getString("ship_to_state");
            info.shipZip = rs.getString("ship_to_zip");
            info.shipCountry = rs.getString("ship_to_country");
            info.shipCountryCode = rs.getString("ship_to_country_code");
            info.shipCustomerNo = rs.getString("ship_to_cust_number");
        }
        else
        {
            info.billCompany = "Unregistered User";
            info.shipCompany = "Unregistered User";
        }
        rs.close();

        sql = "SELECT * FROM userinfo WHERE user_name='" + username + "' ";
        rs = connection.executeQuery("paxar_profile", sql);
        if (rs.next())
        {
            info.contractorEmail = rs.getString("email");
            info.billContact = rs.getString("fullname") + "(" + username + ")";
            info.billPhone = rs.getString("phone");
            info.shipContact = rs.getString("fullname") + "(" + username + ")";
            info.shipPhone = rs.getString("phone");
        }
        rs.close();
        return info;
    }
}
