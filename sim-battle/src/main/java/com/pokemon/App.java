package com.pokemon;

import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class App {
    public static void main(String[] args) throws Exception {
        JSONParser jsonparser = new JSONParser();

        // pokemon 1
        FileReader poke1File = new FileReader("sim-battle\\pokemon-data\\pikachu.json");
        JSONObject pokemon1 = (JSONObject) jsonparser.parse(poke1File);

        JSONObject pokemon1Info = (JSONObject) pokemon1.get("pokemon_info");
        JSONObject poke1Moves = (JSONObject) pokemon1.get("move_set");

        String poke1Name = (String) pokemon1Info.get("name");
        JSONObject poke1Stats = (JSONObject) pokemon1Info.get("stats");
        int poke1level = 9;// (int) pokemon1Info.get("level");

        // Pokemon 2
        FileReader poke2File = new FileReader("sim-battle\\pokemon-data\\bulbasaur.json");
        JSONObject pokemon2 = (JSONObject) jsonparser.parse(poke2File);

        JSONObject pokemon2Info = (JSONObject) pokemon2.get("pokemon_info");
        JSONObject poke2Moves = (JSONObject) pokemon2.get("move_set");

        String poke2Name = (String) pokemon2Info.get("name");
        JSONObject poke2Stats = (JSONObject) pokemon2Info.get("stats");
        int poke2level = 9;// (int) pokemon2Info.get("level").intValue();

        // System.out.println(pokemonInfo);
        Pokemon poke1 = new Pokemon(poke1Name, poke1Stats, poke1Moves, poke1level);
        Pokemon poke2 = new Pokemon(poke2Name, poke2Stats, poke2Moves, poke2level);

        // HARD CODED POKE1 USING ELECTRIC MOVE CALLED "ThunderShock" <--- TEST
        JSONObject poke1MoveChoice = poke1.moveChoice();
        Simulate battle = new Simulate();

        int powerMove = (int) (long) poke1MoveChoice.get("Power");
        int attackStat = (int) (long) poke1.getStat("attack");
        int defenceStat = (int) (long) poke2.getStat("defense");

        // lvl 9 pikachu attacking lvl 9 bulbasaur, using thunderShock (of not very
        // effective and same type attack move)
        System.out.println("Damage to poke2: " + battle.damageFormula(9, powerMove, attackStat, defenceStat, 0.5,
                poke1.getCritChance(), 1, 1.5));
    }
}
