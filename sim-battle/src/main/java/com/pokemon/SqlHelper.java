package com.pokemon;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;

public class SqlHelper {
    private Connection c = null;
    private Statement stmt = null;

    private int nextSimId() {
        return 0;
    }

    private String getConfig() {
        Properties prop = new Properties();
        String url = null;

        try (InputStream input = new FileInputStream(
                "C:\\Users\\Menu\\Documents\\Projects\\Pokemon_sim\\sim-battle\\pokemon-data")) {
            prop.load(input);
            url = prop.getProperty("db.url");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return url;
    }

    public void openConnection() {
        String url = getConfig();

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + url);
            c.setAutoCommit(false);
            stmt = c.createStatement();
            System.out.println("Opened DATABASE");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void closeConnection() {
        try {
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.out.println("Database already closed");
        }
        System.out.println("CLOSED CONNECTION");
    }

    public void createSimulation(String battleName, int pokemon1_id, int pokemon2_id) {

        int sim_id = nextSimId();
        LocalDate currentDate = LocalDate.now();

        String sql = "INSERT INTO Simulation (sim_id, battle_name, entry, pokemon1_id, pokemon2_id)" +
                "VALUES(%o,%s,%t,%o,%o);";
        String sqlFormat = String.format(sql, sim_id, battleName, currentDate, pokemon1_id, pokemon2_id);
        try {
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println("Connection not opened yet");
            System.exit(0);
        }
    }

    public void createBattleStance() {
        System.out.println("Senario");
    }

    public void round() {
        System.out.println("round x created");
    }

    public void pokemonTable() {
        System.out.println("Pokemon profile");
    }
}
