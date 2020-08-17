package com.project0.lawrencedang;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DAO<T> {
    public abstract T get(String pkey) throws SQLException;
    public abstract boolean put(T t) throws SQLException;

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
