package com.pokemon;

import java.time.LocalDate;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;

public class SqlHelper {
    private Connection c = null;

    private String getConfig() {
        Properties prop = new Properties();
        String url = null;

        try (InputStream input = new FileInputStream(
                "C:\\Users\\Menu\\Documents\\Projects\\Pokemon_sim\\sim-battle\\pokemon-data\\config.properties")) {
            prop.load(input);
            url = prop.getProperty("db.url");

        } catch (Exception e) {
            System.err.println(e.getStackTrace()[0].getLineNumber() + " " + e);
            System.exit(0);
        }

        return url;
    }

    public void openConnection() {
        String url = getConfig();

        try {
            Class.forName("org.sqlite.JDBC");

            c = DriverManager.getConnection("jdbc:sqlite:" + url);
            c.setAutoCommit(false);
            // stmt = c.createStatement();
            System.err.println("Opened DATABASE");
        } catch (Exception e) {
            System.err.println(e.getStackTrace()[0].getLineNumber() + " " + e);
            System.exit(0);
        }
    }

    public void closeConnection() {
        try {
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getStackTrace()[0].getLineNumber() + " " + e);
            System.exit(0);
        }
        System.err.println("CLOSED CONNECTION");
    }

    public int getPokeId(String name, long level) {
        String sql = "SELECT pokemon_id FROM pokemon WHERE name = ? and level = ?";
        try (PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setLong(2, level);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                return rs.getInt("pokemon_id");
            }
        } catch (Exception e) {
            System.err.println(e.getStackTrace()[0].getLineNumber() + " " + e);
            System.exit(0);
        }
        return -1;
    }

    public int getSimId(String simBattleName) {
        String sql = "SELECT sim_id FROM simulation WHERE battle_name = ?";
        try (PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setString(1, simBattleName);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                return rs.getInt("sim_id");
            }
        } catch (Exception e) {
            System.err.println(e.getStackTrace()[0].getLineNumber() + " " + e);
            System.exit(0);
        }
        return -1;
    }

    public int getStanceId(int simId) {
        String sql = "SELECT stance_id FROM battleStance WHERE sim_id = ?";
        try (PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setInt(1, simId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                return rs.getInt("stance_id");
            }
        } catch (Exception e) {
            System.err.println(e.getStackTrace()[0].getLineNumber() + " " + e);
            System.exit(0);
        }
        return -1;
    }

    public void insertPokemon(String name, long hp, long attack, long defence, long special_attack,
            long special_defence,
            long speed, long level, Long pokedex_id) throws SQLException {
        String sql = "INSERT INTO pokemon (name, hp, attack, defence, special_attack, special_defence, speed, level, pokedex_id) VALUES (?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setLong(2, hp);
            stmt.setLong(3, attack);
            stmt.setLong(4, defence);
            stmt.setLong(5, special_attack);
            stmt.setLong(6, special_defence);
            stmt.setLong(7, speed);
            stmt.setLong(8, level);
            if (pokedex_id != null) {
                stmt.setLong(9, pokedex_id);
            } else {
                stmt.setNull(9, java.sql.Types.BIGINT);
            }

            stmt.executeUpdate();

        } catch (Exception e) {
            System.err.println(
                    e.getStackTrace()[0].getFileName() + " - " + e.getStackTrace()[0].getLineNumber() + ": " + e);
            System.exit(0);
        }

    }

    public void insertSimulation(String battleName, int pokemon1_id, int pokemon2_id) throws SQLException {
        LocalDate currentDate = LocalDate.now();

        String sql = "INSERT INTO simulation (battle_name, entry_time, pokemon1_id, pokemon2_id) VALUES (?, ?, ?, ?);";

        try (PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setString(1, battleName);
            stmt.setDate(2, java.sql.Date.valueOf(currentDate));
            stmt.setInt(3, pokemon1_id);
            stmt.setInt(4, pokemon2_id);

            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println(
                    e.getStackTrace()[0].getFileName() + " - " + e.getStackTrace()[0].getLineNumber() + ": " + e);
            System.exit(0);
        }
    }

    public void insertBattleStance(int sim_id, String poke1Stance, String poke2Stance) throws SQLException {
        String sql = "INSERT INTO battleStance (sim_id, pokemon1_stance, pokemon2_stance) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setInt(1, sim_id);
            stmt.setString(2, poke1Stance);
            stmt.setString(3, poke2Stance);

            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println(
                    e.getStackTrace()[0].getFileName() + " - " + e.getStackTrace()[0].getLineNumber() + ": " + e);
            System.exit(0);
        }
    }

    public void insertGame(int stance_id, int gameNumber, String winner) throws SQLException {
        String sql = "INSERT INTO game (stance_id, game_number, winner_of_game) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, stance_id);
            stmt.setInt(2, gameNumber);
            stmt.setString(3, winner);

            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println(
                    e.getStackTrace()[0].getFileName() + " - " + e.getStackTrace()[0].getLineNumber() + ": " + e);
            System.exit(0);
        }
    }

    public void insertRound(int gameNumber, int roundNumber, int pokemonId, int opponentId, String moveName,
            String moveType, Boolean wentFirst, Integer damage, Double critDamage, Integer currentHp,
            Boolean roundWinner) throws SQLException {

        String sql = "INSERT INTO round (game_id, round_number, pokemon_id, opponent_id, move_name, move_type, went_first, damage, crit_damage, current_hp, round_winner) VALUES (?,?,?,?,?,?,?,?,?,?,?);";

        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, gameNumber);
            stmt.setInt(2, roundNumber);
            stmt.setInt(3, pokemonId);
            stmt.setInt(4, opponentId);

            setNullableString(stmt, 5, moveName);
            setNullableString(stmt, 6, moveType);
            setNullableBoolean(stmt, 7, wentFirst);
            setNullableInt(stmt, 8, damage);
            setNullableDouble(stmt, 9, critDamage);
            setNullableInt(stmt, 10, currentHp);
            setNullableBoolean(stmt, 11, roundWinner);

            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println(
                    e.getStackTrace()[0].getFileName() + " - " + e.getStackTrace()[0].getLineNumber() + ": " + e);
            System.exit(0);
        }
    }

    private void setNullableString(PreparedStatement stmt, int index, String value) throws SQLException {
        if (value != null) {
            stmt.setString(index, value);
        } else {
            stmt.setNull(index, java.sql.Types.VARCHAR);
        }
    }

    private void setNullableBoolean(PreparedStatement stmt, int index, Boolean value) throws SQLException {
        if (value != null) {
            stmt.setBoolean(index, value);
        } else {
            stmt.setNull(index, java.sql.Types.BOOLEAN);
        }
    }

    private void setNullableInt(PreparedStatement stmt, int index, Integer value) throws SQLException {
        if (value != null) {
            stmt.setInt(index, value);
        } else {
            stmt.setNull(index, java.sql.Types.INTEGER);
        }
    }

    private void setNullableDouble(PreparedStatement stmt, int index, Double value) throws SQLException {
        if (value != null) {
            stmt.setDouble(index, value);
        } else {
            stmt.setNull(index, java.sql.Types.REAL);
        }
    }
}
