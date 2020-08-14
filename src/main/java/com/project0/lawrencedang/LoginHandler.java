package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import static com.project0.lawrencedang.ClientServerProtocol.READY;
import static com.project0.lawrencedang.ClientServerProtocol.REJECT;
import static com.project0.lawrencedang.ClientServerProtocol.MESSAGE_TEMPLATE;

public class LoginHandler extends LoginServerHandler {
    public static final String tokenTemplate = "TOKEN|%s\n";
    public final int tokenLength = 20;
    TokenRepository tokenDao;
    UserRepository userDao;

    public LoginHandler(BufferedReader reader, PrintStream writer)
    {
        super(reader, writer);
        tokenDao = new TokenRepository();
        userDao = new UserRepository();
    }

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
                System.err.println("Problem while getting username from client");
                return;
            }
            registered = userDao.isRegistered(username.getString());
            if(!registered)
            {
                writer.print(String.format(MESSAGE_TEMPLATE, "Username not found"));
            }
            else
            {
                User user = userDao.get();
                String token = generateTokenString();
                putToken(user.getUserId(), token);
                writer.print(String.format(tokenTemplate, token));
            }
        }
        while(!registered);
    }

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

    public void putToken(int userId, String tokenString)
    {
        tokenDao.put(new Token(userId, tokenString));
    }


    
}