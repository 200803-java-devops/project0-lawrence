package com.project0.lawrencedang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TokenRepository extends DAO<Token> {
    public Token get(String tokenString) throws SQLException
    {
        String queryTemplate = "select * from token where token_string = ?";
        try(Connection connection = getConnection())
        {
            PreparedStatement query = connection.prepareStatement(queryTemplate);
            query.setString(1, tokenString);
            ResultSet results = query.executeQuery();
            Token token = null;
            while(results.next())
            {
                String ts  = results.getString("token_string");
                int holder_id = results.getInt("holder");
                token = new Token(holder_id, ts);
            }
            return token;
        }
        catch(SQLException e)
        {
            throw e;
        }
    }

    public boolean put(Token token) throws SQLException
    {
        String queryTemplate = "insert into token(token_string, holder) values(?, ?)";
        try(Connection connection = getConnection())
        {
            PreparedStatement query = connection.prepareStatement(queryTemplate);
            query.setString(1, token.getToken());
            query.setInt(2, token.getUserId());
            int rowsAffected = query.executeUpdate();
            return rowsAffected > 0;
        }
        catch(SQLException e)
        {
            throw e;
        }
    }

    public boolean delete(Token token) throws SQLException
    {
        String queryTemplate = "delete from token where token_string = ?";
        try(Connection connection = getConnection())
        {
            PreparedStatement query = connection.prepareStatement(queryTemplate);
            query.setString(1, token.getToken());
            query.setInt(2, token.getUserId());
            int rowsAffected = query.executeUpdate();
            return rowsAffected > 0;
        }
        catch(SQLException e)
        {
            throw e;
        }
    }

    public boolean tokenExists(String tokenString) throws SQLException
    {
        Token token = get(tokenString);
        return token != null;
    }

}
