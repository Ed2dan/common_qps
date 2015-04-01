package com.paxar.qps.data.editor;
import java.util.*;
import java.io.Serializable;

/**
 * <p>Title: DataEditorSortKey</p>
 *
 * <p>Description: supports DataEditor</p>
 *
 * @author jerome
 * @version 1.0
 */
public class DataEditorSortKey extends Vector implements Serializable
{
    static final long serialVersionUID = -2389231285256764000L;

    final static public int ASC = 1;
    final static public int DESC = -1;

    String currentKey = "";
    int currentOrder = ASC;

    public DataEditorSortKey()
    {
    }

    public final static String separator = ":";

    public void set(String sortKeys[])
    {
        for (int i=0; i<sortKeys.length; i++)
            if (sortKeys[i].indexOf(separator) < 0)
                this.add(sortKeys[i] + separator + sortKeys[i]);
            else
                this.add(sortKeys);
    }

    public String getKey(int index)
    {
        String result = (String) this.get(index);
        return result.split(separator)[0];
    }

    public int getIndex(String key)
    {
        for (int i=0; i< size(); i++)
        {
            String content = ( String )super.get( i );
            if ( content.startsWith( key + separator ) )
                return i;
        }
        return -1;
    }

    public void setCurrentKey(String current, int order)
    {
        currentKey = current;
        currentOrder = order;
    }

    public int getKeyOrder()
    {
        return currentOrder;
    }

    public int getToggleKeyOrder()
    {
        return -currentOrder;
    }

    public void add(String key, String sql_order)
    {
        super.add(key + separator + sql_order);
    }

    public String getSQL(int key, int order)
    {
        return getSQL(getKey(key),order);
    }

    public String getSQL(String key, int order)
    {
        setCurrentKey(key,order);
        for (int i=0; i< size(); i++)
        {
            String content = (String) super.get(i);
            if (content.startsWith(key + separator))
            {
                if (order==ASC)
                    return " ORDER BY " + content.split( separator )[ 1 ];
                else
                {
                    String result = "";
                    String keys[] = content.split( separator )[ 1 ].split(",");
                    for ( int j = 0; j< keys.length; j++)
                    {
                        if (!result.equals(""))
                            result += ",";
                        result += " " + keys[j] + " DESC ";
                    }
                    return " ORDER BY " + result;
                }
            }
        }
        return "";
    }

}
