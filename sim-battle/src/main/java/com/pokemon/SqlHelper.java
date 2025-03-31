package com.pokemon;

import java.time.LocalDate;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;

public class SqlHelper {
    private Connection c = null;
    // private Statement stmt = null;

    private int nextSimId() {
        return 0;
    }

    private String getConfig() {
        Properties prop = new Properties();
        String url = null;

        try (InputStream input = new FileInputStream(
                "C:\\Users\\Menu\\Documents\\Projects\\Pokemon_sim\\sim-battle\\pokemon-data\\config.properties")) {
            prop.load(input);
            url = prop.getProperty("db.url");

        } catch (Exception e) {
            System.out.println(e);
        }

        return url;
    }

    public void openConnection() {
        String url = getConfig();

        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println(url);
            c = DriverManager.getConnection("jdbc:sqlite:" + url);
            c.setAutoCommit(false);
            // stmt = c.createStatement();
            System.err.println("Opened DATABASE");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void closeConnection() {
        try {
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println("Database already closed");
        }
        System.err.println("CLOSED CONNECTION");
    }

    public int getPokeId(String name, long level) {
        String sql = "SELECT pokemon_id FROM pokemon WHERE name = ? and level = ?";
        try {
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setLong(2, level);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                return rs.getInt("pokemon_id");
            }
        } catch (Exception e) {
            System.err.println(e);
            System.exit(0);
        }
        return -1;
    }

    public int getSimId(String simBattleName) {
        String sql = "SELECT sim_id FROM simulate WHERE name = ?";
        try {
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, simBattleName);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                return rs.getInt("sim_id");
            }
        } catch (Exception e) {
            System.err.println(e);
            System.exit(0);
        }
        return -1;
    }

    public void insertPokemon(String name, long hp, long attack, long defence, long special_attack,
            long special_defence,
            long speed, long level) {
        String sql = "INSERT INTO pokemon (name, hp, attack, defence, special_attack, special_defence, speed, level) VALUES (?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setLong(2, hp);
            stmt.setLong(3, attack);
            stmt.setLong(4, defence);
            stmt.setLong(5, special_attack);
            stmt.setLong(6, special_defence);
            stmt.setLong(7, speed);
            stmt.setLong(8, level);

            stmt.executeUpdate();

        } catch (Exception e) {
            System.err.println("Connection not opened yet " + e);
            System.exit(0);
        }

    }

    public void insertSimulation(String battleName, int pokemon1_id, int pokemon2_id) {
        LocalDate currentDate = LocalDate.now();

        String sql = "INSERT INTO simulation (battle_name, entry, pokemon1_id, pokemon2_id) VALUES (?, ?, ?, ?);";

        try {
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, battleName);
            stmt.setDate(2, java.sql.Date.valueOf(currentDate));
            stmt.setInt(3, pokemon1_id);
            stmt.setInt(4, pokemon2_id);

            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    public void insertBattleStance(int sim_id, String poke1Stance, String poke2Stance, int winnerPokeId) {
        String sql = "INSERT INTO battleStance (sim_id, pokemon1_stance, pokemon2_stance, winner_poke_id) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, sim_id);
            stmt.setString(2, poke1Stance);
            stmt.setString(3, poke2Stance);
            stmt.setInt(4, winnerPokeId);

            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    public void insertRound(int simId, String moveCat1, String moveCat2, int attackFt, int poke1Damage,
            int poke2Damage, int matchEnd) {
        int gameId = 0; // DUNNO WHAT THIS WAS FOR AGAIN
        String sql = "INSERT INTO round (stance_id, move_category1, move_category2, attacked_first, pokemon1_damage, pokemon2_damage, match_end, game_id) VALUES (?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(0, simId);
            stmt.setString(1, moveCat1);
            stmt.setString(2, moveCat2);
            stmt.setInt(3, attackFt);
            stmt.setInt(4, poke1Damage);
            stmt.setInt(5, poke2Damage);
            stmt.setInt(6, matchEnd);
            stmt.setInt(7, gameId);

            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    public void insertResultSum(int stanceId, int gameNum, int totalRounds, int Poke1Damage, int poke2Damage,
            int winnerPokeId) {
        String sql = "INSERT INTO resultSummary (stance_id, game_number,total_rounds, pokemon1_damage, pokemon2_damage, winner_poke_id) VALUES (?,?,?,?,?,?)";

        try {
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }
}
