package com.pokemon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void sqlConnect() {
        SqlHelper db = new SqlHelper();
        db.openConnection();
        db.closeConnection();
    }

    @Test
    public void sqlGetId() {
        SqlHelper db = new SqlHelper();
        db.openConnection();
        int id = db.getPokeId("pikachu", 9);
        System.out.println(id);
        db.closeConnection();
        assertEquals(0, id);
    }
}
