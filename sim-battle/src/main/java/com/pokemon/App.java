package com.pokemon;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class App {
    private static Pokemon[] battlingPokemon = new Pokemon[2];
    private static Helper hf = new Helper();
    private static List<Game> allGames;
    public static HashMap<String, Object> poke1round;
    public static HashMap<String, Object> poke2round;
    public static List<HashMap<String, Object>> round;
    static int damage;

    public static void simBattle() throws Exception {
        allGames = new ArrayList<>();
        int poke1Wins = 0;
        int poke2Wins = 0;

        for (int battles = 0; battles < 10; battles++) {
            Game inGame = new Game(battlingPokemon[0], battlingPokemon[1], "normal", "normal");

            int deadPokemon = 0; // Dunno how to have it blank, but probably wont be an issue
            int rounds = 0;
            boolean battleOver = false;
            int starting;

            while (!battleOver) {
                round = new ArrayList<>();
                poke1round = new HashMap<String, Object>();
                poke2round = new HashMap<String, Object>();

                System.out.println("\nRound: " + rounds);
                JSONObject poke1Move = battlingPokemon[0].moveChoice();
                JSONObject poke2Move = battlingPokemon[1].moveChoice();

                starting = (long) battlingPokemon[0].getStat("speed") > (long) battlingPokemon[1].getStat("speed") ? 0
                        : 1;

                for (int i = 0; i < 2; i++) {
                    System.out.println(battlingPokemon[starting].getName() + "<---- ATTACKING");
                    if (i == 0) {
                        poke1round.put("name", battlingPokemon[starting].getName());
                        poke2round.put("name", battlingPokemon[starting ^ 1].getName());
                        poke1round.put("started", true);
                        poke2round.put("started", false);
                    }

                    if (starting == 0) {
                        damage = getDamageMove(battlingPokemon, poke1Move, starting);
                        poke1round.put("move", poke1Move);
                        poke1round.put("damage", damage);
                    } else {
                        damage = getDamageMove(battlingPokemon, poke2Move, starting);
                        poke2round.put("move", poke2Move);
                        poke2round.put("damage", damage);
                    }

                    System.out.println(battlingPokemon[starting].getName() + " did " + damage);

                    battlingPokemon[starting ^ 1].reduceHealth(damage);

                    if (battlingPokemon[starting ^ 1].isDead()) {
                        deadPokemon = starting ^ 1;
                        battleOver = true;

                        if ((starting ^ 1) == 0) {
                            inGame.setWinner(battlingPokemon[starting].getName());
                            poke1round.put("isWinner", false);
                            poke2round.put("isWinner", true);
                        } else {
                            inGame.setWinner(battlingPokemon[starting ^ 1].getName());
                            poke1round.put("isWinner", true);
                            poke2round.put("isWinner", false);
                        }
                        starting ^= 1;
                        break;
                    }

                    // Switch to next pokemon
                    starting ^= 1;
                }

                poke1round.put("currentHp", battlingPokemon[starting].getHealth());
                poke2round.put("currentHp", battlingPokemon[starting ^ 1].getHealth());

                // What this HELL AM I doing. Not sure if this is good or not
                round.add(poke1round);
                round.add(poke2round);
                inGame.addRound(round);

                rounds++;
            }

            if (deadPokemon == 0) {
                poke2Wins++;
            } else {
                poke1Wins++;
            }
            System.out.println("Game End, dead pokemon: " + battlingPokemon[deadPokemon].getName());

            hf.resetPokemon(battlingPokemon);
            allGames.add(inGame);
        }
        System.out.println("\nWins - " + battlingPokemon[0].getName() + ": " + poke1Wins + " "
                + battlingPokemon[1].getName() + ": "
                + poke2Wins);
        // for (int round = 0; round++ < (allGames.get(0).getAllRounds().size() - 1);) {
        // System.out.println("Game 1; Round " + round + "; " +
        // allGames.get(0).aRound(round) + "\n");
        // }
        // hf.exportGamesToJson(allGames);
        hf.newSimToDB(allGames);
    }

    public static int getDamageMove(Pokemon[] battlingPokemon, JSONObject move, int starting) {
        Integer power = (move.get("power") != null) ? ((Number) move.get("power")).intValue() : null;

        if (power == null) {
            // Status SHIT NEEDS TO BE DONE
            System.out.println("MINOR ERROR: " + move.get("category"));
            if (starting == 0) {
                poke1round.put("effectiveType1", null);
                poke1round.put("effectiveType2", null);
                poke1round.put("STAB", null);
                poke1round.put("wasCrit", null);
            } else {
                poke2round.put("effectiveType1", null);
                poke2round.put("effectiveType2", null);
                poke2round.put("STAB", null);
                poke2round.put("wasCrit", null);
            }
            return 0;

        } else {

            String moveStatAttack = move.get("category").equals("special") ? "special-attack" : "attack";
            // Gen 1 Pokemon special is the same. User will have to specify if they are
            // doing gen 1 or not.
            // Code will only change when calculation base level stat is changed at python
            // level.
            String moveStatDefence = move.get("category").equals("special") ? "special-defense" : "defense";
            int attackingStat = (int) (long) battlingPokemon[starting].getStat(moveStatAttack);
            int defendingStat = (int) (long) battlingPokemon[starting ^ 1].getStat(moveStatDefence);

            double effectiveAgainstType1 = hf.effectiveTypeAgainst((String) move.get("type"),
                    battlingPokemon[starting ^ 1].getType1());
            double effectiveAgainstType2 = hf.effectiveTypeAgainst((String) move.get("type"),
                    battlingPokemon[starting ^ 1].getType2());

            double STAB = battlingPokemon[starting].sameTypeAttackBase((String) move.get("type"));

            double critChance = battlingPokemon[starting].getCritChance();
            int damage = hf.damageFormula(battlingPokemon[starting].getLevel(),
                    power, attackingStat, defendingStat, effectiveAgainstType1, effectiveAgainstType2,
                    critChance, STAB);

            System.out.println(starting);
            if (starting == 0) {
                poke1round.put("effectiveType1", effectiveAgainstType1);
                poke1round.put("effectiveType2", effectiveAgainstType2);
                poke1round.put("STAB", STAB);
                poke1round.put("wasCrit", critChance);
            } else {
                poke2round.put("effectiveType1", effectiveAgainstType1);
                poke2round.put("effectiveType2", effectiveAgainstType2);
                poke2round.put("STAB", STAB);
                poke2round.put("wasCrit", critChance);
            }

            System.out.println("Move Category: " + move.get("category"));
            return damage;
        }
    }

    public static void challangers() throws IOException, ParseException {
        JSONParser jsonparser = new JSONParser();

        // pokemon 1
        FileReader poke1File = new FileReader("sim-battle\\pokemon-data\\pikachu.json");
        JSONObject pokemon1 = (JSONObject) jsonparser.parse(poke1File);
        // Pokemon 2
        FileReader poke2File = new FileReader("sim-battle\\pokemon-data\\bulbasaur.json");
        JSONObject pokemon2 = (JSONObject) jsonparser.parse(poke2File);

        JSONObject[] pokemonObj = { pokemon1, pokemon2 };

        // Get data from JSON File
        int n = 0;
        for (JSONObject i : pokemonObj) {
            JSONObject pokemonInfo = (JSONObject) i.get("pokemon_info");

            String name = (String) pokemonInfo.get("name");
            int level = 9; // (int) pokemon1Info.get("level");
            JSONObject lvlBaseStat = (JSONObject) i.get("lvl_base_stat");
            JSONObject moveSet = (JSONObject) i.get("move_set");
            String type1 = (String) pokemonInfo.get("type1");
            String type2 = (String) pokemonInfo.get("type2");

            Pokemon createPokemone = new Pokemon(name, lvlBaseStat, moveSet, level, type1, type2);
            battlingPokemon[n] = createPokemone;

            n++;
        }
    }

    public static void main(String[] args) throws Exception {
        challangers();
        simBattle();
    }
}
