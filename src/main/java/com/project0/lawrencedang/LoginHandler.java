package com.project0.lawrencedang;

import static com.project0.lawrencedang.ClientServerProtocol.REJECT;
import static com.project0.lawrencedang.ClientServerProtocol.TOKEN_TEMPLATE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A LoginHandler handles logins from a client. Upon a successful login, the LoginHandler creates a new token in the database and sends the client
 * the token string.
 */
public class LoginHandler extends LoginServerHandler {
    public final int tokenLength = 20;
    TokenRepository tokenDao;
    UserRepository userDao;
    final static Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    /**
     * Create a new login handler that receives input on the BufferedReader and sends messages through the PrintStream
     */
    public LoginHandler(BufferedReader reader, PrintStream writer)
    {
        super(reader, writer);
        tokenDao = new TokenRepository();
        userDao = new UserRepository();
    }

    /**
     * Process and respond to client login.
     */
    public void run()
    {
        Username username = new Username();
        boolean registered = false;
        do
        {
            try
            {
                username = getUsername();
            }
            catch(IOException e)
            {
                logger.warn("Problem while getting username from client");
                return;
            }

            try
            {
                registered = userDao.isRegistered(username.getString());
            }
            catch(SQLException e)
            {
                logger.warn("Problem while checking username");
                return;
            }

            if(!registered)
            {
                logger.debug("User entered a non-registered name");
                writer.print(REJECT);
            }
            else
            {
                User user = null;
                try
                {
                    user = userDao.getByName(username.getString());
                }
                catch(SQLException e)
                {
                    logger.warn("Problem while getting user from database");
                    return;
                }
                
                String token = generateTokenString();
                putToken(user.getUserId(), token);
                writer.print(String.format(TOKEN_TEMPLATE, token));
                logger.info("Generated new token " + token);
            }
        }
        while(!registered);
    }

    /**
     * Creates a new token string. A token string is an alphanumeric sequence.
     */
    public String generateTokenString()
    {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String token = "";
        for(int i = 0; i<tokenLength; i++)
        {
            token += chars.charAt((int)(Math.random() * chars.length()));
        }

        return token;
    }

    /**
     * Puts the token string in the database and associates it with the user id.
     */
    public void putToken(int userId, String tokenString)
    {
        try
        {
            tokenDao.put(new Token(userId, tokenString));
        }
        catch(SQLException e)
        {
            logger.warn("Failed to register token.");
            return;
        }
    }


    
}