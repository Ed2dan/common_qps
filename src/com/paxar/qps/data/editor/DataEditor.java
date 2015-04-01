package com.paxar.qps.data.editor;
import com.paxar.qps.data.AbstractDAO;
import java.sql.ResultSet;
import java.util.*;
import com.paxar.sql.Esc;
import java.io.Serializable;
/**
 * <p>Title: DataEditor </p>
 *
 * <p>Description: class for generic table management</p>
 *
 * @author jerome
 * @version 1.0
 */
public class DataEditor extends AbstractDAO implements Serializable
{
    static final long serialVersionUID = -2677815597676164002L;
    protected String database = "";
    protected String selectSQL = "";
    protected String insertKeys[] = null;
    protected String updateKeys[] = null;
    protected String primaryKeys[] = null;
    protected String deleteKeys[] = null;
    protected String insertTable = "";
    protected String updateTable = "";
    protected String deleteTable = "";
    protected DataEditorRecord records[] = null;
    protected DataEditorSortKey sortKey = null;
    protected int sortKeyIndex = 0;

    public DataEditor()
    {
        reset();
    }

    /**
     * Reset variables
     */
    public void reset()
    {
        database = "";
        selectSQL = "";
        updateTable = "";
        insertTable = "";
        deleteTable = "";
        primaryKeys = null;
        insertKeys = null;
        updateKeys = null;
        deleteKeys = null;
        records = null;
        sortKey = null;
        sortKeyIndex = 0;
    }

    /**
     * Set database using
     * @param database String
     */
    public void setDatabase( String database )
    {
        this.database = database;
    }

    /**
     * Function to insert record in one statement
     * @param table String
     * @param keys String[]
     * @param record DataEditorRecord
     */

    public boolean insert( String table, String keys[], String primary[], DataEditorRecord record ) throws
        Exception
    {
        this.setInsertTable( table );
        this.setInsertKeys( keys );
        this.setPrimaryKeys(primary);
        if (checkExist(record))
            return false;
        executeInsertSQL( record );
        return true;
    }

    /**
     * Function to get records in one statement
     * @param sql String
     * @return DataEditorRecord[]
     * @throws Exception
     */
    public DataEditorRecord[] select( String sql ) throws Exception
    {
        return select( sql, null, null, 0 );
    }

    public DataEditorRecord[] select( String sql, DataEditorSortKey key, DataEditorNavigation denav ) throws Exception
    {
        return select( sql, key, key.getKey( denav.getSortBy() ), denav.getSortOrder() );
    }

    public DataEditorRecord[] select( String sql, DataEditorSortKey key, String sort_by, int sort_order ) throws
        Exception
    {
        this.setSelectSQL( sql );
        this.setSortKey( key );
        executeSelectSQL( sort_by, sort_order );
        return this.getData();
    }

    /**
     * Fucntion to update records in one statement
     * @param sql String
     * @param keys String[]
     * @param record DataEditorRecord
     * @throws Exception
     */
    public void update( String table, String keys[], String primarykeys[], DataEditorRecord record ) throws Exception
    {
        this.setUpdateTable( table );
        this.setPrimaryKeys( primarykeys );
        this.setUpdateKeys(keys);
        this.excuteUpdateSQL( record );
    }

    /**
     * Function to delete records in one statement
     * @param table String
     * @param keys String[]
     * @param record DataEditorRecord
     * @throws Exception
     */
    public void delete(String table, String keys[], DataEditorRecord record) throws Exception
    {
        this.setDeleteTable(table);
        this.setDeleteKeys(keys);
        this.executeDeleteSQL(record);
    }

    /**
     * Set Select Statement
     * @param sql String
     */
    public void setSelectSQL( String sql )
    {
        selectSQL = sql;
    }

    /**
     * set which key to sort
     * @param key DataEditorSortKey
     */
    public void setSortKey( DataEditorSortKey key )
    {
        sortKey = key;
    }

    /**
     * Execute select
     * @throws Exception
     */
    public void executeSelectSQL( int sort_by, int sort_order ) throws Exception
    {
        if ( sortKey == null )
            throw new Exception( "Sort Key Not Set" );
        executeSelectSQL( sortKey.getKey( sort_by ), sort_order );
    }

    public void executeSelectSQL( String sort_by, int sort_order ) throws Exception
    {
        checkConnection();
        String sql = selectSQL;
        if ( ( sortKey != null ) && ( sort_by != null ) )
            sql += sortKey.getSQL( sort_by, sort_order );
        ResultSet rs = connection.executeQuery( database, sql );
        Vector vc = new Vector();
        while ( rs.next() )
        {
            DataEditorRecord record = new DataEditorRecord();
            record.setData( rs );
            vc.add( record );
        }
        rs.close();
        records = new DataEditorRecord[vc.size() ];
        vc.copyInto( records );
    }

    /**
     * Get data from select SQL
     * @return DataEditorRecord[]
     */
    public DataEditorRecord[] getData()
    {
        return records;
    }

    /**
     * Set primary keys for updating records
     * @param keys String[]
     */
    public void setPrimaryKeys( String keys[] )
    {
        primaryKeys = removeNumberSign(keys);
    }

    /**
     * Set update sql statement
     * @param sql String
     */
    public void setUpdateTable( String table )
    {
        updateTable = table;
    }

    public void setUpdateKeys( String keys[])
    {
        updateKeys = removeNumberSign(keys);
    }

    /**
     * execute update statement
     * @param record DataEditorRecord
     * @throws Exception
     */
    public void excuteUpdateSQL( DataEditorRecord record ) throws Exception
    {
        checkConnection();
        String sql = "UPDATE " + updateTable + " SET ";

        for (int i=0; i< updateKeys.length; i++)
        {
            if ( !sql.endsWith( " SET " ) )
                sql += ",";
            sql += " " + updateKeys[ i ] + "=";
            if ( record.isInt( updateKeys[ i ] ) )
                sql += Esc.escape( record.getString( updateKeys[ i ] ) );
            else
            {
                if (record.getString( updateKeys[ i ] )==null)
                    sql += "''";
                else
                    sql += "'" + Esc.escape( record.getString( updateKeys[ i ] ) ) + "'";
            }
        }
        sql += " WHERE true ";
        for ( int i = 0; i < primaryKeys.length; i++ )
        {
            sql += " AND " + getSQLCriteria( record, primaryKeys[ i ] );
        }
        connection.executeUpdate( database, sql );
    }

    /**
     * generic sql criteria script
     * @param record DataEditorRecord
     * @param field String
     * @return String
     */
    protected static String getSQLCriteria( DataEditorRecord record, String field )
    {
        String sql = "";

        if ( record.isInt( field ) )
            sql += "(" + field + "=" + record.getString( field ) + ")";
        else
        {
            sql += "(" + field + "='" + Esc.escape( record.getString( field ) ) + "' ";
            if ( record.getString( field ).equals( "" ) )
                sql += " OR " + field + " IS NULL ";
            sql += ")";
        }

        return sql;
    }

    /**
     * set delete table
     * @param table String
     */
    public void setDeleteTable( String table )
    {
        deleteTable = table;
    }

    /**
     * Set search key to delete reocrds
     * @param keys String[]
     */
    public void setDeleteKeys( String keys[] )
    {
        deleteKeys = removeNumberSign(keys);
    }

    /**
     * Run delete sql
     * @param record DataEditorRecord
     * @throws Exception
     */
    public void executeDeleteSQL( DataEditorRecord record ) throws Exception
    {
        checkConnection();
        String sql = "DELETE FROM " + deleteTable + " WHERE true ";
        for ( int i = 0; i < deleteKeys.length; i++ )
        {
            sql += " AND " + getSQLCriteria( record, deleteKeys[ i ] );
        }
        connection.executeUpdate( database, sql );
    }

    /**
     * Set insert table
     * @param table String
     */
    public void setInsertTable( String table )
    {
        insertTable = table;
    }

    /**
     * Set insert SQL keys
     * @param keys String[]
     */
    public void setInsertKeys( String keys[] )
    {
        insertKeys = removeNumberSign(keys);
    }

    /**
     * Insert data
     * @param record DataEditorRecord
     * @throws Exception
     */
    public void executeInsertSQL( DataEditorRecord record ) throws Exception
    {
        checkConnection();
        String sql = "";
        sql += "INSERT INTO " + insertTable + " (";
        for ( int i = 0; i < insertKeys.length; i++ )
        {
            if ( !sql.endsWith( "(" ) )
                sql += ",";
            sql += insertKeys[ i ];
        }
        sql += ") VALUES(";
        for ( int i = 0; i < insertKeys.length; i++ )
        {
            if ( !sql.endsWith( "(" ) )
                sql += ",";
            if ( record.isInt( insertKeys[ i ] ) )
                sql += Esc.escape( record.getString( insertKeys[ i ] ) );
            else
                sql += "'" + Esc.escape( record.getString( insertKeys[ i ] ) ) + "'";
        }
        sql += ")";
        connection.executeUpdate(database,sql);
    }

    /**
     * Check if the record is already exist
     * @param record DataEditorRecord
     * @return boolean
     * @throws Exception
     */
    public boolean checkExist(DataEditorRecord record) throws Exception
    {
        checkConnection();
        String sql = "";
        sql += "SELECT 1 FROM " + insertTable + " WHERE true ";
        for ( int i = 0; i < primaryKeys.length; i++ )
       {
           sql += " AND " + getSQLCriteria( record, primaryKeys[ i ] );
       }
       ResultSet rs = connection.executeQuery(database, sql);
       boolean result = false;
       if (rs.next())
           result = true;
       rs.close();
       return result;
    }

    /**
     * Remove # sign
     * @param src String[]
     * @return String[]
     */
    public String[] removeNumberSign(String src[])
    {
        String result[] = new String[src.length];

        for (int i=0; i<src.length; i++)
        {
            result[i] = src[i].replaceAll("#","");
        }
        return result;
    }

    /**
     * Get select where criteria
     * @param keys String[]
     * @param map request.getParameterMap
     */
    public static String getSelectCriteria(String keys[], Map map)
    {
        DataEditorRecord record = new DataEditorRecord(keys, map);

        String sql = "";
        for (int i=0; i< keys.length; i++)
        {
            if ( !sql.equals( "" ) )
                sql += " AND ";
            sql += getSQLCriteria( record, keys[ i ] );
        }
        return sql;
    }

    public void executeSQL (String sql) throws Exception
    {
        checkConnection();
        connection.executeUpdate( database, sql );
    }
}




