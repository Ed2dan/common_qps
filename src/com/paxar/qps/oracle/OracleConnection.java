package com.paxar.qps.oracle;
import oracle.jdbc.driver.*;
import java.sql.*;


/**
 * @author Jerome
 *
 * OracleConnection defines a Oracle connection.
 * <p>
 * Here provides direct data access to Oracle for common information
 * retrieval, such as bill, ship, shipping method, country and etc.
 *
 */

public class OracleConnection
{

    /**
     * The Oracle account could access to the following tables readonly.
     *
     * apps.fnd_territories_tl
     * apps.wsh_carrier_services
     * apps.wsh_org_carrier_services
     * APPS.RA_CUSTOMERS
     * APPS.RA_ADDRESSES_ALL
     * APPS.RA_SITE_USES_ALL
     * APPS.RA_CONTACTS
     * APPS.RA_PHONES
     * APPS.HZ_CONTACT_POINTS
    */
    public static final String dbHost = "PXRDB2.PAXAR.COM";
    public static final int dbPort = 1521;
    public static final String dbName = "prod";
    public static final String dbUsername = "d2commref";
    public static final String dbPassword = "oracle23";
    public static final int dbLoginTimeout = 20;

    protected Connection conn;
    public OracleConnection() throws SQLException
    {
        this.createConnection();
    }

    /**
     * Create Oracle connection
     * @throws SQLException
     */
    protected void createConnection() throws SQLException
    {
        DriverManager.setLoginTimeout(dbLoginTimeout);
        DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
        conn = DriverManager.getConnection
                (getConnectionString(), dbUsername, dbPassword);
    }

    /**
     * Get connection strin
     * @return String
     */
    protected String getConnectionString()
    {
        return "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)" +
           "(HOST=" + dbHost + ")(PORT=" + dbPort + ")))" +
           "(CONNECT_DATA=(SERVICE_NAME=" + dbName + ")(SERVER=DEDICATED)))";
    }

    /**
     * Must close connection after use to save resources.
     * @throws SQLException
     */
    protected void closeConnection()
    {
        try{
            conn.close();
        } catch (Exception e) {}
    }

    /**
     * Execute query
     * @param sql String
     * @return ResultSet
     * @throws SQLException
     */
    public ResultSet executeQuery(String sql) throws SQLException
    {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        //System.out.println("Oracle: " + sql);
        return rs;
    }

    /**
     * Execute update (This should not be used as we have readonly acess)
     * @param sql String
     * @return int
     * @throws SQLException
     */
    public int executeUpdate(String sql) throws SQLException
    {
        Statement stmt = conn.createStatement();
        return stmt.executeUpdate(sql);
    }

    /**
     * Close after use
     * @throws SQLException
     */
    public void close()
    {
        this.closeConnection();
    }
}
