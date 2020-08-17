package com.project0.lawrencedang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class UserRepositoryTest {
    UserRepository repo;
    @Before
    public void setup()
    {
        repo = new UserRepository();
    }

    @Test
    public void getByIdTest() throws SQLException
    {
        User user = repo.get("1");
        assertNotNull(user);
        assertEquals(user.getUsername(), "test");
        assertEquals(user.getUserId(), 1);

        User user2 = repo.get("9999999");
        assertNull(user2);
    }

    @Test
    public void getByNameTest() throws SQLException
    {
        User user = repo.getByName("test");
        assertNotNull(user);
        assertEquals(user.getUsername(), "test");
        assertEquals(user.getUserId(), 1);

        User user2 = repo.getByName("ThisNameShouldntExist1111");
        assertNull(user2);
    }

    @Test
    public void usernameExistsTest() throws SQLException
    {
        assertTrue(repo.isRegistered("test"));
        assertFalse(repo.isRegistered("ThisNameShouldntExist1111"));

    }
}