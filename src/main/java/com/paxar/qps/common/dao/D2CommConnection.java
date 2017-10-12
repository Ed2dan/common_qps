package com.paxar.qps.common.dao;

import com.paxar.dbconnect.AbstractConnection;
import com.paxar.dbconnect.ConnectionInfo;

import java.sql.*;

/**
 * Created by bandr on 11.08.2016.
 */
public class D2CommConnection {

    private AbstractConnection connection = null;

    D2CommConnection(AbstractConnection connection) {
        this.connection = connection;
    }


    public String getDataBaseName() {
        return connection.getDataBaseName();
    }

    public String getHostName() {
        return connection.getHostName();
    }

    public boolean isOld() {
        return connection.isOld();
    }

    public boolean getIsAvailable() {
        return connection.getIsAvailable();
    }

    public void setIsAvailable(boolean isAvailable) {
        connection.setIsAvailable(isAvailable);
    }

    public void open(String dataBase) throws DatabaseException {
        try {
            connection.open(dataBase);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public void open(String host, String dataBase) throws DatabaseException {
        try {
            connection.open(host, dataBase);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public void close() {
    }

    public ResultSet executeQuery(String strQuery) throws DatabaseException {
        try {
            return connection.executeQuery(strQuery);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public ResultSet executeQuery(String strQuery, int resultSetType, int resultSetConcurrency) throws DatabaseException {
        try {
            return connection.executeQuery(strQuery, resultSetType, resultSetConcurrency);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public void executeUpdate(String strUpdate) throws DatabaseException {
        try {
            connection.executeUpdate(strUpdate);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public void setAutoCommit(boolean autoCommit) throws DatabaseException {
        try {
            connection.setAutoCommit(autoCommit);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public void commit() throws DatabaseException {
        try {
            connection.commit();
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public void rollback() throws DatabaseException {
        try {
            connection.rollback();
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public void releaseLock() {
        connection.releaseLock();
    }

    public ConnectionInfo getInfo(ConnectionInfo parent) {
        return connection.getInfo(parent);
    }

    public PreparedStatement prepareStatement(String statement) throws DatabaseException {
        try {
            return connection.prepareStatement(statement);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public Array createArrayOf(String typeName, Object[] elements) throws DatabaseException {
        try {
            return connection.createArrayOf(typeName, elements);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }
}
