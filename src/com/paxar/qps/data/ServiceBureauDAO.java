/*
 * $RCSfile: ServiceBureauDAO.java,v $
 * $Revision: 1.9 $
 * $Date: 2008/10/16 13:17:56 $
 *
 * Copyright 2005 Paxar, Inc. All rights reserved.
 */
package com.paxar.qps.data;

import java.sql.ResultSet;
import java.util.Vector;
import javax.mail.internet.InternetAddress;

/**
 * The class <code>ServiceBureauDAO</code> provides data
 * access functions to Service Bureau information.
 *
 * @author  Ty Busby
 */
public class ServiceBureauDAO extends AbstractDAO
{
    /**
     * Returns a distinct list of service bureaus for the
     * specified retailer.
     *
     * @param retailer String
     * @return String[]
     * @throws Exception
     */
    public static String[] getServiceBureaus( String retailer ) throws Exception
    {
        Vector bureauList = new Vector( );
        String sql = "SELECT DISTINCT service_bureau FROM service_bureau " +
                     "WHERE retailer = '" + retailer + "'";
        ResultSet rsBureaus = connection.executeQuery( "paxar_profile", sql );
        while ( rsBureaus.next( ))
        {
            bureauList.add( rsBureaus.getString( 1 ));
        }

        String bureaus[] = new String[bureauList.size( )];
        bureauList.copyInto( bureaus );
        return bureaus;
    }

    /**
     * Returns a list of email addreeses for the specified retailer
     * and the service bureau listed in the {@link ContactInfo}
     * object.  If the ContactInfo object contains a valid contractor
     * email address, this is added to the list as well.
     *
     * @param retailer String
     * @param info ContactInfo
     * @return InternetAddress[]
     * @throws Exception
     */
    public static InternetAddress[] getEmails( String retailer, ContactInfo info ) throws Exception
    {
        InternetAddress addresses[] = ServiceBureauDAO.getEmails( retailer, info.serviceBureau );
        if ( info != null && info.contractorEmail != null &&
                             info.contractorEmail.trim().length() > 0 )
        {
            InternetAddress addressesEx[] = new InternetAddress[addresses.length + 1];
            for ( int i = 0; i < addresses.length; i++ )
            {
                addressesEx[i] = addresses[i];
            }
            addressesEx[addressesEx.length - 1] = new InternetAddress( info.contractorEmail );
            return addressesEx;
        }
        return addresses;
    }
    
    public static InternetAddress[] getEmails( String retailer, OracleInfo info ) throws Exception
    {
        InternetAddress addresses[] = ServiceBureauDAO.getEmails( retailer, info.getValue( OracleInfo.service_bureau ));
        if ( info != null && info.getValue( OracleInfo.contractor_email ) != null &&
        		info.getValue( OracleInfo.contractor_email ).trim( ).length() > 0 )
        {
            InternetAddress addressesEx[] = new InternetAddress[addresses.length + 1];
            for ( int i = 0; i < addresses.length; i++ )
            {
                addressesEx[i] = addresses[i];
            }
            addressesEx[addressesEx.length - 1] = new InternetAddress( info.getValue( OracleInfo.contractor_email ));
            return addressesEx;
        }
        return addresses;
    }

    /**
     * Returns a list of email addresses for the specified retailer
     * and service bureau.
     *
     * @param retailer String
     * @param serviceBureau String
     * @return InternetAddress[]
     * @throws Exception
     */
    public static InternetAddress[] getEmails( String retailer, String serviceBureau )
                                        throws Exception
    {
        Vector eMailList = new Vector( );
        String sql = "SELECT DISTINCT email FROM service_bureau " +
                     "WHERE retailer = '" + retailer + "' AND " +
                     "service_bureau = '" + serviceBureau + "' AND " +
                     "( TRIM( email ) != '' AND email IS NOT NULL )";
        ResultSet rsEmails = connection.executeQuery( "paxar_profile", sql );
        while ( rsEmails.next( ))
        {
            eMailList.add( new InternetAddress( rsEmails.getString( 1 )));
        }

        InternetAddress[] emails = new InternetAddress[eMailList.size( )];
        eMailList.copyInto( emails );
        return emails;
    }

    /**
     * Returns a list of users from where the service bureau site matches
     * the user site AND the specified retailer is setup for that user.
     *
     * @param retailer String
     * @param serviceBureau String
     * @return String[]
     * @throws Exception
     */
    public static String[] getUsers( String retailer, String serviceBureau ) throws Exception
    {
        Vector userList = new Vector( );
        String sql = "SELECT userinfo.user_name FROM userinfo " +
                     "INNER JOIN retailers ON " +
                     "   retailers.user_name = userinfo.user_name AND " +
                     "   retailers.retailer = '" + retailer + "' " +
                     "INNER JOIN service_bureau ON" +
                     "   service_bureau.site = userinfo.site AND " +
                     "   service_bureau.service_bureau = '" + serviceBureau + "' AND " +
                     "   service_bureau.retailer = '" + retailer + "'";

        ResultSet rsUsers = connection.executeQuery( "paxar_profile", sql );
        while ( rsUsers.next( ))
        {
            userList.add( rsUsers.getString( 1 ));
        }

        String users[] = new String[userList.size( )];
        userList.copyInto( users );
        return users;
    }
}
