package com.project0.lawrencedang;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Superclass for data access objects that interface with the database.
 * @param <T> The type representing an entry in the database i.e. a user or token
 */
public abstract class DAO<T> {
    public abstract T get(String pkey) throws SQLException;
    public abstract boolean put(T t) throws SQLException;

    /**
     * Attempts to establish a connection to the database and returns the connection.
     * @return the Connection object to access the database.
     * @throws SQLException
     */
    protected Connection getConnection() throws SQLException
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/blackjackdb", "blackjackdb", "ABC123");
        }
        catch(SQLException e)
        {
            System.err.println("Failed to establish a connection to the database.");
            throw e;
        }
        return connection;
    }
}
