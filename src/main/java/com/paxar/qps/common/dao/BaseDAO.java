package com.paxar.qps.common.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import com.averydennison.data.AbstractDAO;

/**
 * <p>Provides better abstraction for creating DAO classes in comparison with {@link AbstractDAO}</p>
 * @author rsav
 * @version 1.0
 *
 */
public abstract class BaseDAO extends AbstractDAO {
    
    private static final Logger logger = Logger.getLogger(BaseDAO.class);
    
    /**
     * Host of database server
     */
    private String dbHost;
    /**
     * Name of database
     */
    private String dbName;
    
    /**
     * Creates DAO instance, sets {@link #dbHost} and {@link #dbName} fields and performs DAO initialization
     * (see {@link AbstractDAO#initialize(String)}). 
     * @param dbHost, not empty 
     * @param dbName, not empty
     * @throws IllegalArgumentException if any argument is null
     */
    public BaseDAO(String dbHost, String dbName) {
        Validate.notEmpty(this.dbHost = dbHost);
        Validate.notEmpty(this.dbName = dbName);
        
        AbstractDAO.initialize(dbHost);
    }
    
    /**
     * Returns database host field value (see {@link #dbHost}).
     * @return DB host field value
     */
    public String getDbHost() {
        return dbHost;
    }
    
    /**
     * Returns database name field value (see {@link #dbName}).
     * @return DB name field value
     */
    public String getDbName() {
        return dbName;
    }
    
    /**
     * Executes specified SQL script.
     * @param sql script, not empty
     * @return Result set of executed script
     * @throws IllegalArgumentException if script is empty
     * @throws DatabaseException if it was failed to execute script
     */
    protected ResultSet execute(String sql) throws DatabaseException {
        Validate.notEmpty(sql);
        try {
            checkConnection();
            return connection.executeQuery(this.dbName, sql);
        } catch (Exception e) {
            logger.error(String.format("Unexpected exception just occurred during executing script [%s]", sql), e);
            throw new DatabaseException("Unexpected database exception just occurred", e);
        }
    }
    
    /**
     * Executes specified script
     * @param sql script, not empty
     * @throws IllegalArgumentException if script is empty
     * @throws DatabaseException if it was failed to execute script
     */
    protected void executeUpdate(String sql) throws DatabaseException {
        Validate.notEmpty(sql);
        try {
            checkConnection();
            connection.executeUpdate(this.dbName, sql);
        } catch (Exception e) {
            logger.error(String.format("Unexpected exception just occurred during executing script [%s]", sql), e);
            throw new DatabaseException("Unexpected database exception just occurred", e);
        }
    }
    
    /**
     * <p>Escapes parameter value.</p>
     * <p>Calls {@link AbstractDAO#esc(String)} method.</p>
     * @param sql
     * @throws IllegalArgumentException if argument is null.
     * @return Escaped value
     */
    protected String escape(String sql) {
        Validate.notNull(sql);
        return AbstractDAO.esc(sql);
    }
    

    /**
     * Executes sql script and returns map with given column key-values.
     * 
     * @param sql
     *            script to execute
     * @param columnName1
     *            key of the map entry
     * @param columnName2
     *            value of the map entry                              
     * @return
     * @throws DatabaseException
     */
    protected Map<String, String> executeToStringMap(String sql, String columnName1, String columnName2) throws DatabaseException {
        Validate.notEmpty(sql, "[sql] parameter cannot be null or empty");        
        Validate.notEmpty(columnName1, "[columnName1] parameter cannot be null or empty");
        Validate.notEmpty(columnName2, "[columnName2] parameter cannot be null or empty");

        try (ResultSet rs = execute(sql)) {
            Map<String, String> result = new HashMap<>();
            while (rs.next()) {
                result.put(rs.getString(columnName1), rs.getString(columnName2));
            }
            return result;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
    
    /**
     * Executes sql script and returns given column values.
     * 
     * @param sql
     *            script to execute
     * @param columnName
     *            column to return
     * @return
     * @throws DatabaseException
     */
    protected List<String> executeToStringList(String sql, String columnName) throws DatabaseException {
        Validate.notEmpty(sql, "[sql] parameter cannot be null or empty");        
        Validate.notEmpty(columnName, "[columnName] parameter cannot be null or empty");

        try (ResultSet rs = execute(sql)) {
            List<String> result = new ArrayList<>();
            while (rs.next()) {
                result.add(rs.getString(columnName));
            }
            return result;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

}