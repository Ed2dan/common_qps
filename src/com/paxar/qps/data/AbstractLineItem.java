/*
 * $RCSfile: AbstractLineItem.java,v $
 * $Revision: 1.4 $
 * $Date: 2006/12/13 09:00:07 $
 *
 * Copyright 2005 Paxar, Inc. All rights reserved.
 */
package com.paxar.qps.data;

import java.sql.ResultSet;
import java.io.Serializable;

/**
 * The class <code>AbstractLineItem</code> provides generic
 * data storage for line item data.  Classes that inherit
 * from this object can use these methods and data structures
 * to save data returned from a DAO query.
 *
 * @author  Ty Busby
 */
public abstract class AbstractLineItem implements Serializable
{
    static final long serialVersionUID = 6938368861792015908L;
    protected String columnNames[];
    public String[] getColumns( ) { return columnNames; }
    protected String columnValues[];
    public String[] getValues( ) { return columnValues; }

    /**
     * Use a ResultSet to create an array of
     * String values to store the data.  Make
     * sure the ResultSet has been positioned
     * before calling this method.
     *
     * @param rsData ResultSet
     * @throws Exception
     */
    public void setData( ResultSet rsData ) throws Exception
    {
        columnNames = new String[rsData.getMetaData( ).getColumnCount( )];
        columnValues = new String[columnNames.length];
        for ( int i = 0; i < columnNames.length; i++ )
        {
            columnNames[i] = rsData.getMetaData( ).getColumnName( i + 1 );
            columnValues[i] = rsData.getString( i + 1 );
            if ( columnValues[i] == null ) { columnValues[i] = ""; }
        }
    }

    /**
     * Return the String data value located at the
     * column index.
     *
     * @param column int
     * @return String
     */
    public String getValue( int column )
    {
        return columnValues[column];
    }

    /**
     * Return the number of columns in this data.
     * @return int
     */
    public int getColumnCount( )
    {
        return columnValues.length;
    }
}
