package com.pokemon;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class App {
    private static Pokemon[] battlingPokemon = new Pokemon[2];
    private static Helper hf = new Helper();

    public static void simBattle() throws Exception {
        int poke1Wins = 0;
        int poke2Wins = 0;

        for (int battles = 0; battles < 1; battles++) {
            int deadPokemon = 0; // Dunno how to have it blank, but probs wont be an issue
            int rounds = 0;
            boolean battleOver = false;
            int starting;

            while (!battleOver) {
                System.out.println("\nRound: " + rounds);
                JSONObject poke1Move = battlingPokemon[0].moveChoice();
                JSONObject poke2Move = battlingPokemon[1].moveChoice();

                starting = (long) battlingPokemon[0].getStat("speed") > (long) battlingPokemon[1].getStat("speed") ? 0
                        : 1;

                for (int i = 0; i < 2; i++) {
                    System.out.println(battlingPokemon[starting].getName() + "<---- ATTACKING");
                    int damage = starting == 0 ? getDamageMove(battlingPokemon, poke1Move, starting)
                            : getDamageMove(battlingPokemon, poke2Move, starting);

                    System.out.println(battlingPokemon[starting].getName() + " did " + damage);

                    battlingPokemon[starting ^ 1].reduceHealth(damage);
                    if (battlingPokemon[starting ^ 1].isDead()) {
                        deadPokemon = starting ^ 1;
                        battleOver = true;
                        break;
                    }

                    // Switch to next pokemon
                    starting = starting ^ 1;
                }
                rounds++;
            }

            if (deadPokemon == 0) {
                poke2Wins++;
            } else {
                poke1Wins++;
            }
            System.out.println("Game End, dead pokemon: " + battlingPokemon[deadPokemon].getName());

            hf.resetPokemon(battlingPokemon);
        }
        System.out.println("\nWins - " + battlingPokemon[0].getName() + ": " + poke1Wins + " "
                + battlingPokemon[1].getName() + ": "
                + poke2Wins);
    }

    public static int getDamageMove(Pokemon[] battlingPokemon, JSONObject move, int starting) {
        Integer power = (move.get("power") != null) ? ((Number) move.get("power")).intValue() : null;

        if (power == null) {
            // Status SHIT NEEDS TO BE DONE
            System.out.println("MINOR ERROR: " + move.get("category"));
            return 0;

        } else {

            String moveStatAttack = move.get("category").equals("special") ? "special-attack" : "attack";
            // Gen 1 Pokemon special is the same. User will have to specify if they are doing gen 1 or not.
            // Code will only change when calculation baselevelstat is changed at python level.
            String moveStatDefense = move.get("category").equals("special") ? "special-defense" : "defense";
            int attackingStat = (int) (long) battlingPokemon[starting].getStat(moveStatAttack);
            int defendingStat = (int) (long) battlingPokemon[starting ^ 1].getStat(moveStatDefense);

            double effectiveAgainstType1 = hf.effectiveTypeAgainst((String) move.get("type"),
                    battlingPokemon[starting ^ 1].getType1());
            double effectiveAgainstType2 = hf.effectiveTypeAgainst((String) move.get("type"),
                    battlingPokemon[starting ^ 1].getType2());

            double STAB = battlingPokemon[starting ^ 1].sameTypeAttackBase((String) move.get("type"));

            int damage = hf.damageFormula(battlingPokemon[starting].getLevel(),
                    power, attackingStat, defendingStat, effectiveAgainstType1, effectiveAgainstType2,
                    battlingPokemon[starting].getCritChance(), STAB);

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
