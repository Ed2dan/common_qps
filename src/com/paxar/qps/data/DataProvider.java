package com.paxar.qps.data;

import java.io.Serializable;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DataProvider implements Serializable
{
	private static final long serialVersionUID = -4259286045112759595L;

	public static SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );

    protected boolean inDb = false;
    public boolean getInDb( )
    {
        return inDb;
    }

    public DataProvider( ResultSet rs ) throws Exception
    {
        data = new String[rs.getMetaData( ).getColumnCount( )];
        for ( int i = 0; i < data.length; i++ )
        {
            String value = rs.getString( i + 1 );
            data[i] = value == null ? "" : value;
        }
        inDb = true;
    }

    public DataProvider( int capacity )
    {
        data = new String[capacity];
        for ( int i = 0; i < data.length; i++ )
        {
            data[i] = "";
        }
        inDb = false;
    }

    protected String data[] = new String[0];

    public String getValue( int index )
    {
        return data[index] == null ? "" : data[index];
    }

    public void setValue( int index, long value )
    {
        data[index] = "" + value;
    }

    public void setValue( int index, String value )
    {
        data[index] = value == null ? "" : value;
    }

    public String getValueHtml( int index )
    {
        return getValue( index ).equals( "" ) ? "&nbsp;" : getValue( index );
    }

    public long getValueNumber( int index )
    {
        return Long.parseLong( getValue( index ));
    }

    public String getValueDate( int index )
    {
        SimpleDateFormat parse = new SimpleDateFormat( "yyyyMMdd" );
        try
        {
            return format.format( parse.parse( getValue( index )));
        } catch ( ParseException e )
        {
            return getValue( index );
        }
    }
}
