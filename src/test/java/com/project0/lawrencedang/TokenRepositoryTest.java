package com.project0.lawrencedang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class TokenRepositoryTest {
    TokenRepository repo;
    @Before
    public void setup()
    {
        repo = new TokenRepository();
    }

    @Test
    public void getTokenByStringTest() throws SQLException
    {
        Token token = repo.get("test");
        assertNotNull(token);
        assertEquals(token.getToken(), "test");
        assertEquals(token.getUserId(), 1);

        Token token2 = repo.get("faketoken");
        assertNull(token2);
    }

    @Test
    public void tokenExistsTest() throws SQLException
    {
        assertTrue(repo.tokenExists("test"));
        assertFalse(repo.tokenExists("faketoken"));
    }
}