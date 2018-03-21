package com.paxar.qps.common.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.paxar.qps.common.config.D2CommProperties;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import com.averydennison.data.AbstractDAO;

/**
 * <p>Provides better abstraction for creating DAO classes in comparison with {@link AbstractDAO}</p>
 *
 * @author rsav
 * @version 1.0
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
     *
     * @param dbHost, not empty
     * @param dbName, not empty
     * @throws IllegalArgumentException if any argument is null
     */
    public BaseDAO(String dbHost, String dbName) {
        Validate.notEmpty(this.dbHost = dbHost);
        Validate.notEmpty(this.dbName = dbName);

        AbstractDAO.initialize(dbHost);
    }

    public BaseDAO(String dbName) {
        this(D2CommProperties.DB_HOST, dbName);
    }


    /**
     * Returns database host field value (see {@link #dbHost}).
     *
     * @return DB host field value
     */
    public String getDbHost() {
        return dbHost;
    }

    /**
     * Returns database name field value (see {@link #dbName}).
     *
     * @return DB name field value
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * Executes specified SQL script.
     *
     * @param sql script, not empty
     * @return Result set of executed script
     * @throws IllegalArgumentException if script is empty
     * @throws DatabaseException        if it was failed to execute script
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
     *
     * @param sql script, not empty
     * @throws IllegalArgumentException if script is empty
     * @throws DatabaseException        if it was failed to execute script
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
     *
     * @param sql
     * @return Escaped value
     * @throws IllegalArgumentException if argument is null.
     * @return Escaped value
     */
    protected String escape(String sql) {
        Validate.notNull(sql);
        return AbstractDAO.esc(sql);
    }

    /**
     * Performs {@link #escape(String)} method on each value of specified set and returns new set withb escaped values.
     *
     * @param values set with values that should be escaped
     * @return new {@link HashSet} instance with escaped values from original set
     * @throws IllegalArgumentException if specified set is null, is empty or contains null elements
     */
    protected Set<String> escapeSet(Set<String> values) {
        Validate.notNull(values);
        Validate.notEmpty(values);
        Validate.noNullElements(values);

        final Set<String> result = new HashSet<>();

        for (String value : values) {
            result.add(escape(value));
        }

        return result;
    }


    /**
     * Executes sql script and returns map with given column key-values.
     *
     * @param sql         script to execute
     * @param columnName1 key of the map entry
     * @param columnName2 value of the map entry
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
     * @param sql        script to execute
     * @param columnName column to return
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

    protected D2CommConnection getConnection() throws DatabaseException {
        try {
            return new D2CommConnection(connection.getConnection(dbName));
        } catch (Exception e) {
            logger.error("Error acquiring connection:", e);
            throw new DatabaseException("An exception occurred trying to acquire connection:", e);
        }
    }

    protected PreparedStatement prepareStatement(String sql) throws DatabaseException {
        Validate.notEmpty(sql, "[sql] parameter cannot be null or empty");
        try {
            return connection.prepareStatement(dbName, sql);
        } catch (Exception e) {
            logger.error("Error preparing statement", e);
            throw new DatabaseException(e);
        }
    }

    protected PreparedStatement prepareStatement(String sql, String... args) throws DatabaseException {
        PreparedStatement preparedStatement = prepareStatement(sql);
        if (args != null) {
            setPreparedStatementArguments(preparedStatement, args);
        }
        return preparedStatement;
    }

    protected PreparedStatement prepareStatement(D2CommConnection d2CommConnection, String sql, String... args) throws DatabaseException {
    	try{
	        PreparedStatement preparedStatement = d2CommConnection.prepareStatement(sql);
	        if (args != null) {
	            setPreparedStatementArguments(preparedStatement, args);
	        }
	        return preparedStatement;
    	} finally {
    		if(d2CommConnection != null) {
    			d2CommConnection.releaseLock();
    		}
    	}
    }

    protected PreparedStatement prepareStatement(D2CommConnection d2CommConnection, String sql, List<Object> args) throws DatabaseException {
    	try{
	    	PreparedStatement preparedStatement = d2CommConnection.prepareStatement(sql);
	        passArguments(preparedStatement, args);
	        return preparedStatement;
    	} finally {
    		if(d2CommConnection != null) {
    			d2CommConnection.releaseLock();
    		}
    	}
    }

    protected PreparedStatement prepareStatement(String sql, List<Object> args) throws DatabaseException {
        return prepareStatement(getConnection(), sql, args);
    }

    private void passArguments(PreparedStatement preparedStatement, List<Object> args) throws DatabaseException {
        PreparedStatementArgumentSetter.passArguments(preparedStatement, args);
    }

    private void setPreparedStatementArguments(PreparedStatement preparedStatement, String[] args) throws DatabaseException {
        try {
            for (int i = 1; i <= args.length; i++) {
                preparedStatement.setString(i, args[i - 1]);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
