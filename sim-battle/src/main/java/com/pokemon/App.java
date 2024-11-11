package com.pokemon;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class App {
    private static Pokemon[] battlingPokemon = new Pokemon[2];
    private static Helper hf = new Helper();

    public static void simBattle() {
        String deadPokemon = "Broken";

        for (int battles = 0; battles < 1; battles++) {
            boolean battleOver = false;
            int starting;

            while (!battleOver) {

                JSONObject poke1Move = battlingPokemon[0].moveChoice();
                JSONObject poke2Move = battlingPokemon[1].moveChoice();

                // Who starts?
                starting = (long) battlingPokemon[0].getStat("speed") > (long) battlingPokemon[1].getStat("speed") ? 0
                        : 1;

                for (int i = 0; i < 2; i++) {
                    // Get damage
                    int damage = starting == 0 ? getDamageMove(battlingPokemon, poke1Move, starting)
                            : getDamageMove(battlingPokemon, poke2Move, starting);

                    battlingPokemon[starting ^ 1].reduceHealth(damage);
                    if (battlingPokemon[starting ^ 1].isDead()) {
                        deadPokemon = battlingPokemon[starting ^ 1].getName();
                        battleOver = true;
                    }
                    // Switch to next pokemon
                    starting = starting ^ 1;
                }
            }
            System.out.println("Game End, dead pokemon: " + deadPokemon);
        }
    }

    public static int getDamageMove(Pokemon[] battlingPokemon, JSONObject move, int starting) {
        // Data retrieve
        int attackingStat = (int) (long) battlingPokemon[starting].getStat("attack");
        int defendingStat = (int) (long) battlingPokemon[starting ^ 1].getStat("defense");

        Integer power = (Integer) move.get("power");
        double effectiveAgainstType1 = hf.effectiveTypeAgainst((String) move.get("type"),
                battlingPokemon[starting ^ 1].getType1());
        double effectiveAgainstType2 = hf.effectiveTypeAgainst((String) move.get("type"),
                battlingPokemon[starting ^ 1].getType2());
        double STAB = battlingPokemon[starting ^ 1].sameTypeAttackBase((String) move.get("type"));

        if (power == null) {
            // Haven't done this shit yet (effects and buffs )
            System.err.println(move.get("category"));
            return 0;
        } else {
            // Damage output
            int damage = hf.damageFormula(battlingPokemon[starting].getLevel(),
                    power,
                    attackingStat, defendingStat, effectiveAgainstType1, effectiveAgainstType2,
                    battlingPokemon[starting].getCritChance(), STAB);
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
            JSONObject stats = (JSONObject) pokemonInfo.get("stats");
            JSONObject moveSet = (JSONObject) i.get("move_set");
            String type1 = (String) pokemonInfo.get("type1");
            String type2 = (String) pokemonInfo.get("type2");

            Pokemon createPokemone = new Pokemon(name, stats, moveSet, level, type1, type2);
            battlingPokemon[n] = createPokemone;

            n++;
        }
    }

    public static void main(String[] args) throws Exception {
        challangers();
        simBattle();
    }
}
