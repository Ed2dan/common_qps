package com.paxar.qps.common.dao;

import com.averydennison.data.AbstractDAO;
import com.paxar.qps.common.config.D2CommProperties;
import com.paxar.qps.common.utils.stream.ThrowingLambdaUtils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * <p>Provides better abstraction for creating DAO classes in comparison with {@link AbstractDAO}</p>.
 *
 * @author rsav
 * @version 1.0
 */
public abstract class BaseDAO extends AbstractDAO {

    private static final Logger logger = Logger.getLogger(BaseDAO.class);
    private static final String UNEXPECTED_DATABASE_EXCEPTION_JUST_OCCURRED
            = "Unexpected database exception just occurred";
    private static final String UNEXPECTED_EXCEPTION_JUST_OCCURRED_DURING_EXECUTING_SCRIPT
            = "Unexpected exception just occurred during executing script [%s]";
    private static final String SQL_PARAMETER_CANNOT_BE_NULL_OR_EMPTY = "[sql] parameter cannot be null or empty";

    /**
     * Host of database server.
     */
    private String dbHost;
    /**
     * Name of database.
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
        Validate.notEmpty(dbHost);
        Validate.notEmpty(dbName);
        this.dbHost = dbHost;
        this.dbName = dbName;
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
     * @throws DatabaseException if it was failed to execute script
     */
    protected ResultSet execute(String sql) throws DatabaseException {
        Validate.notEmpty(sql);
        try {
            checkConnection();
            return connection.executeQuery(this.dbName, sql);
        } catch (Exception e) {
            logger.error(String.format(UNEXPECTED_EXCEPTION_JUST_OCCURRED_DURING_EXECUTING_SCRIPT, sql), e);
            throw new DatabaseException(UNEXPECTED_DATABASE_EXCEPTION_JUST_OCCURRED, e);
        }
    }

    /**
     * Executes specified script.
     *
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
            logger.error(String.format(UNEXPECTED_EXCEPTION_JUST_OCCURRED_DURING_EXECUTING_SCRIPT, sql), e);
            throw new DatabaseException(UNEXPECTED_DATABASE_EXCEPTION_JUST_OCCURRED, e);
        }
    }

    /**
     * Executes specified scripts.
     *
     * @param queries scripts, not empty
     * @throws IllegalArgumentException if script is empty
     * @throws DatabaseException if it was failed to execute script
     */
    protected void executeUpdate(List<String> queries) throws DatabaseException {
        Validate.notEmpty(queries);
        try {
            checkConnection();
            connection.executeUpdate(this.dbName, new Vector<>(queries));
        } catch (Exception e) {
            logger.error(String.format(UNEXPECTED_EXCEPTION_JUST_OCCURRED_DURING_EXECUTING_SCRIPT, queries), e);
            throw new DatabaseException(UNEXPECTED_DATABASE_EXCEPTION_JUST_OCCURRED, e);
        }
    }

    /**
     * <p>Escapes parameter value.</p>
     * <p>Calls {@link AbstractDAO#esc(String)} method.</p>
     *
     * @return Escaped value
     * @throws IllegalArgumentException if argument is null.
     */
    protected String escape(String sql) {
        Validate.notNull(sql);
        return AbstractDAO.esc(sql);
    }

    /**
     * Performs {@link #escape(String)} method on each value of specified set and returns new set with escaped values.
     *
     * @param values set with values that should be escaped
     * @return new {@link HashSet} instance with escaped values from original set
     * @throws IllegalArgumentException if specified set is null, is empty or contains null elements
     */
    protected Set<String> escapeSet(Set<String> values) {
        Validate.notNull(values);
        Validate.notEmpty(values);
        Validate.noNullElements(values);
        return values.stream().map(this::escape).collect(Collectors.toSet());
    }


    /**
     * Executes sql script and returns map with given column key-values.
     *
     * @param sql script to execute
     * @param columnName1 key of the map entry
     * @param columnName2 value of the map entry
     */
    protected Map<String, String> executeToStringMap(String sql, String columnName1, String columnName2)
            throws DatabaseException {
        Validate.notEmpty(sql, SQL_PARAMETER_CANNOT_BE_NULL_OR_EMPTY);
        Validate.notEmpty(columnName1, "[columnName1] parameter cannot be null or empty");
        Validate.notEmpty(columnName2, "[columnName2] parameter cannot be null or empty");

        try (ResultSet rs = execute(sql)) {
            return Stream.of(rs)
                    .filter(f -> ThrowingLambdaUtils.wrap(f::next))
                    .collect(Collectors.toMap(
                            key -> ThrowingLambdaUtils.wrap(() -> key.getString(columnName1)),
                            value -> ThrowingLambdaUtils.wrap(() -> value.getString(columnName2))
                            ));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Executes sql script and returns given column values.
     *
     * @param sql script to execute
     * @param columnName column to return
     */
    protected List<String> executeToStringList(String sql, String columnName) throws DatabaseException {
        Validate.notEmpty(sql, SQL_PARAMETER_CANNOT_BE_NULL_OR_EMPTY);
        Validate.notEmpty(columnName, "[columnName] parameter cannot be null or empty");

        try (ResultSet rs = execute(sql)) {
            return Stream.of(rs)
                    .filter(f -> ThrowingLambdaUtils.wrap(f::next))
                    .map(f -> ThrowingLambdaUtils.wrap(() -> f.getString(columnName)))
                    .collect(Collectors.toList());
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
        Validate.notEmpty(sql, SQL_PARAMETER_CANNOT_BE_NULL_OR_EMPTY);
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
            passArguments(preparedStatement, Arrays.asList(args));
        }
        return preparedStatement;
    }

    protected PreparedStatement prepareStatement(D2CommConnection d2CommConnection, String sql, String... args)
            throws DatabaseException {
        try {
            PreparedStatement preparedStatement = d2CommConnection.prepareStatement(sql);
            if (args != null) {
                passArguments(preparedStatement, Arrays.asList(args));
            }
            return preparedStatement;
        } finally {
            if (d2CommConnection != null) {
                d2CommConnection.releaseLock();
            }
        }
    }

    protected PreparedStatement prepareStatement(D2CommConnection d2CommConnection, String sql, List<Object> args)
            throws DatabaseException {
        try {
            PreparedStatement preparedStatement = d2CommConnection.prepareStatement(sql);
            passArguments(preparedStatement, args);
            return preparedStatement;
        } finally {
            if (d2CommConnection != null) {
                d2CommConnection.releaseLock();
            }
        }
    }

    private void passArguments(PreparedStatement preparedStatement, List<Object> args) throws DatabaseException {
        PreparedStatementArgumentSetter.passArguments(preparedStatement, args);
    }

}
