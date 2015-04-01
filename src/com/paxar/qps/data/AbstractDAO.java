/*
 * $RCSfile: AbstractDAO.java,v $
 * $Revision: 1.4 $
 * $Date: 2006/01/17 14:28:21 $
 *
 * Copyright 2005 Paxar, Inc. All rights reserved.
 */
package com.paxar.qps.data;

import com.paxar.dbconnect.ConnectionManager;
import com.paxar.dbconnect.PaxarConnectionManager;

/**
 * The class <code>AbstractDAO</code> provides generic
 * data access functionality including intializing a
 * connection manager and checking for a valid connection.
 *
 * @author  Ty Busby
 */
public abstract class AbstractDAO
{
    protected static ConnectionManager connection = null;

    /**
     * Initialize the connection manager on the specified
     * host.
     *
     * @param dbHost String
     */
    public static synchronized void initialize( String dbHost )
    {
        if ( connection == null )
        {
            connection = PaxarConnectionManager.getInstance( dbHost );
        }
    }

    /**
     * Check the current connection status by querying the
     * connection manager reference for null.
     *
     * @throws Exception
     */
    protected static void checkConnection( ) throws Exception
    {
        if ( connection == null )
        {
            throw new RuntimeException( "Invalid state for Data Access Object.  " +
                                        "Call initialize first. " );
        }
    }
}
