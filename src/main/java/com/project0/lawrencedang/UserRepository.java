package com.project0.lawrencedang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository extends DAO<User>{
    @Override
    public User get(String uid) throws SQLException
    {
        String queryTemplate = "select * from user_entry where id = ?";
        try(Connection connection = getConnection())
        {
            PreparedStatement query = connection.prepareStatement(queryTemplate);
            query.setInt(1, Integer.valueOf(uid));
            ResultSet results = query.executeQuery();
            User user = null;
            while(results.next())
            {
                int id = results.getInt("id");
                String username = results.getString("username");
                user = new User(id, username);
            }
            return user;
        }
        catch(SQLException e)
        {
            throw e;
        }
    }

    public User getByName(String username) throws SQLException
    {
        String queryTemplate = "select * from user_entry where username = ?";
        try(Connection connection = getConnection())
        {
            PreparedStatement query = connection.prepareStatement(queryTemplate);
            query.setString(1, username);
            ResultSet results = query.executeQuery();
            User user = null;
            while(results.next())
            {
                int id = results.getInt("id");
                String uname = results.getString("username");
                user = new User(id, uname);
            }
            return user;
        }
        catch(SQLException e)
        {
            throw e;
        }
    }

    @Override
    public boolean put(User user) throws SQLException
    {
        return put(user.getUsername());
    }
    public boolean put(String username) throws SQLException
    {
        String queryTemplate = "insert into user_entry(username) values(?)";
        try(Connection connection = getConnection())
        {
            PreparedStatement query = connection.prepareStatement(queryTemplate);
            query.setString(1, username);
            int rowsAffected = query.executeUpdate();
            return rowsAffected > 0;
        }
        catch(SQLException e)
        {
            throw e;
        }
    }

    public boolean isRegistered(String username) throws SQLException
    {
        User user = getByName(username);
        return user != null;
    }
}
