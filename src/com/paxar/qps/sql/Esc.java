/*
 * $RCSfile: Esc.java,v $
 * $Revision: 1.1 $
 * $Date: 2005/07/06 16:54:55 $
 *
 * Copyright 2005 Paxar, Inc. All rights reserved.
 */
package com.paxar.qps.sql;

/**
 * The class <code>Esc</code> provides a static function to
 * double up single quotes and backslashes so that the SQL
 * statements are correct.
 *
 * @author  Ty Busby
 */
public class Esc {

    /**
     * Usage:</br>
     *
     * Esc.escape( "Jim's Steak House \ Grill" );
     * returns the SQL compliant: "Jim''s Steak House \\ Grill"
     *
     * @param s String
     * @return String
     */
    public static final String escape( String s )
    {
        char find[] =      { '\'', '\\'   };
        String replace[] = { "''", "\\\\" };

        String returnString;

        for ( int i = 0; i < find.length; i++ )
        {
            returnString = "";
            for ( int j = 0; j < s.length( ); j++ )
            {
                if ( s.charAt( j ) == find[i] )
                {
                    returnString += replace[i];
                } else
                {
                    returnString += s.charAt( j );
                }
            }
            s = returnString;
        }
        return s;
    }
}
