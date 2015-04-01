package com.paxar.qps.data.editor;
import java.util.*;
import java.sql.*;
import java.io.*;
import com.paxar.qps.data.UnicodeString;

/**
 * <p>Title: DataEditorRecord</p>
 *
 * <p>Description: support class for DataEditor, handles one record</p>
 *
 * @author jerome
 * @version 1.0
 */
public class DataEditorRecord extends HashMap implements Serializable
{
    static final long serialVersionUID = -353590151919014366L;
    public DataEditorRecord()
    {
    }
    public DataEditorRecord(String structure[], Map map)
    {
        setData(structure, map);
    }

    /**
     * Set record from recordset
     * @param rs ResultSet
     * @throws Exception
     */
    public void setData(ResultSet rs) throws Exception
    {
        ResultSetMetaData meta = rs.getMetaData();

        for (int i=1; i<= meta.getColumnCount(); i++)
        {
            if (meta.getColumnType(i)==Types.INTEGER)
                this.put(meta.getColumnName(i), new Integer(rs.getInt(meta.getColumnName(i))));
            else
            {
                if ( rs.getString( meta.getColumnName( i ) ) == null || rs.getString( meta.getColumnName( i ) ).equals("null"))
                    this.put(meta.getColumnName(i), "");
                else
                    this.put( meta.getColumnName( i ), rs.getString( meta.getColumnName( i ) ) );
            }
        }
    }

    /**
     * set Data
     * @param structure String[] It is the same as field names; add # sign at the front to note as Interger field
     * @param map Map
     * @throws Exception
     */
    public void setData(String structure[], Map map)
    {
        for (int i=0; i < structure.length; i++)
        {
            if (structure[i].startsWith("#"))
            {
                try
                {
                    String value = (( String[] )map.get( structure[ i ].substring(1) ))[0];
                    value = UnicodeString.getUnicodeFromHTML(value);
                    this.put( structure[ i ].substring( 1 ),
                              new Integer( Integer.parseInt( value ) ) );
                }
                catch ( Exception e )
                {
                    //System.out.println("form value " + structure[ i ] + " not found");
                    this.put( structure[ i ].substring( 1 ), new Integer( 0 ) );
                }
            }
            else
            {
                try
                {
                    String value = ( ( String[] )map.get( structure[ i ] ) )[ 0 ];
                    value = UnicodeString.getUnicodeFromHTML(value);
                    if ( value == null )
                        value = "";
                    this.put( structure[ i ], value );
                }
                catch (Exception e)
                {
                    //System.out.println("form value " + structure[ i ] + " not found");
                    this.put(structure[i], "");
                }
            }
        }
    }

    /**
     * Check if an integer field
     * @param key String
     * @return boolean
     */
    public boolean isInt(String key)
    {
        if (this.get(key)==null)
            return false;
        if ( this.get( key ).getClass().getName().toUpperCase().indexOf( "INTEGER" ) >= 0 )
            return true;
        return false;
    }

    /**
     * Get data in string
     * @param key String
     * @return String
     */
    public String getString(String key)
    {
        if (this.get(key)==null)
            return null;

        if (isInt(key))
            return "" + getInt(key);
        return  UnicodeString.getHTMLFromUnicode((String) get(key));
    }

    /**
     * Get data in integer
     * @param key String
     * @return int
     */
    public int getInt(String key)
    {
        return ((Integer) get(key)).intValue();
    }

    /**
     * Debug output
     * @return String
     */
    public String debug()
    {
        String result = "";
        Iterator i= this.entrySet().iterator();
        while(i.hasNext())
        {
            Map.Entry e = (Map.Entry) i.next();
            result += e.getKey() + ": " + e.getValue() + " ;\n";
        }
        return result;
    }

}
