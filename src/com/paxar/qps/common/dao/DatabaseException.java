package com.paxar.qps.common.dao;

/**
 * Throws if some error occurred during working with database.
 * @author rsav
 *
 */
public class DatabaseException extends Exception {

    private static final long serialVersionUID = -5417599364901686927L;

    public DatabaseException() {
        super();
    }

    public DatabaseException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }

}
