package com.pokemon;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// import org.json.simple.JSONObject;

public class Helper {
    private String[] strengthElements = { "normal", "fire", "water", "electric", "grass", "ice", "fighting",
            "poison", "ground", "flying", "psychic", "bug", "rock", "ghost", "dragon" };
    private double[][] strengthChart = {
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0.5, 0, 1, 1, 0.5 },
            { 1, 0.5, 0.5, 1, 2, 2, 1, 1, 1, 1, 1, 2, 0.5, 1, 0.5, 2 },
            { 1, 2, 0.5, 1, 0.5, 1, 1, 1, 2, 1, 1, 1, 2, 1, 0.5, 1, 1 },
            { 1, 1, 2, 0.5, 0.5, 1, 1, 1, 0, 2, 1, 1, 1, 1, 0.5, 1, 1 },
            { 1, 1, 2, 0.5, 0.5, 1, 1, 1, 2, 1, 1, 1, 1, 0.5, 1, 1 },
            { 1, 0.5, 2, 1, 0.5, 1, 1, 0.5, 2, 0.5, 1, 0.5, 2, 1, 0.5, 1, 0.5 },
            { 1, 0.5, 0.5, 1, 2, 0.5, 1, 1, 2, 2, 1, 1, 1, 1, 2, 1, 0.5 },
            { 2, 1, 1, 1, 1, 2, 1, 0.5, 1, 0.5, 0.5, 0.5, 2, 0, 1, 2, 2 },
            { 1, 1, 1, 1, 2, 1, 1, 0.5, 0.5, 1, 1, 1, 0.5, 0.5, 1, 1, 0 },
            { 1, 2, 1, 2, 0.5, 1, 1, 2, 1, 0, 1, 0.5, 2, 1, 1, 1, 2 },
            { 1, 1, 1, 0.5, 2, 1, 2, 1, 1, 1, 1, 2, 0.5, 1, 1, 1, 0.5 },
            { 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 0.5, 1, 1, 1, 1, 0, 0.5 },
            { 1, 0.5, 1, 1, 2, 1, 0.5, 0.5, 1, 0.5, 2, 1, 1, 0.5, 1, 2, 0.5 },
            { 1, 2, 1, 1, 1, 2, 0.5, 1, 0.5, 2, 1, 2, 1, 1, 1, 1, 0.5 },
            { 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 0.5, 0.5 },
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 0.5 },
            { 1, 1, 1, 1, 1, 1, 0.5, 1, 1, 1, 2, 1, 1, 2, 1, 0.5, 0.5 },
            { 1, 0.5, 0.5, 0.5, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 0.5 } };

    public int damageFormula(double level, double power, double attackStat, double defenseStat, double type1,
            double type2, double critChance, double stab) {
        Random random = new Random();
        double critRandom = random.nextDouble();
        double randomModif = (217.0 + random.nextInt(38)) / 255.0; // Random between 217 and 255, divided by 255

        double critical = (critRandom < critChance) ? 2.0 : 1.0;

        // Basic damage
        double part1 = ((2.0 * level * critical) / 5.0);
        double stat = attackStat / defenseStat;
        double part2 = (part1 + 2.0) * power * stat;
        double damage = ((part2 / 50.0) + 2.0);

        // Modifier
        damage *= stab;
        damage *= type1;
        damage *= type2;
        damage *= randomModif;
        // for (int i = 217; i <= 255; i++){
        // System.out.println(Math.floor((damage*i)/255));
        // }

        return Math.max(1, (int) damage);
    }

    public void burn_poisenEffect() {

    }

    public int paralysisEffect(int pokeSpeed) {
        return (int) (pokeSpeed - (Math.floor(pokeSpeed * .75)));
    }

    private int sleepCount = 0;

    public int sleepEffect(int isEffect) {
        Random rand = new Random();
        sleepCount++;

        if (isEffect == 0) { // Not asleep, check for if it should be asleep or not
            if (sleepCount > 7) {
                sleepCount = 1;
            }

        } else if (isEffect == 1) { // Is asleep, check for if it shakes it off
            if (sleepCount == 1 && rand.nextInt(4) == 0) { // 1/4 chance
                return 0;
            } else if (sleepCount >= 2 && sleepCount <= 7 && rand.nextInt(8) == 0) { // 1/8 chance for 2-7 turns
                return 0;
            }
        }
        return 1;
    }

    public double effectiveTypeAgainst(String attackType, String defenceType) {
        int indexAttackType = -1;
        int indexDefenceType = -1;

        if (defenceType == null || attackType == null) {
            return 1;
        }

        for (int i = 0; i < strengthElements.length; i++) {
            if (strengthElements[i].equals(attackType)) {
                indexAttackType = i;
            }
            if (strengthElements[i].equals(defenceType)) {
                indexDefenceType = i;
            }
        }

        return strengthChart[indexAttackType][indexDefenceType];
    }

    public void resetPokemon(Pokemon[] battlingPokemon) {
        battlingPokemon[0].resetAll();
        battlingPokemon[1].resetAll();
    }

    public void exportGamesToJson(List<Game> battleSim) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("sim-battle\\pokemon-data\\game_data.json")) {
            gson.toJson(battleSim, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newExportToDB(List<Game> battleSim) {
        // Export all of the simulated battle
        JSONObject jsonP1 = battleSim.get(0).getPokemon(1).getAllStats();
        JSONObject jsonP2 = battleSim.get(0).getPokemon(2).getAllStats();

        String poke1Name = battleSim.get(0).getPokemon(0).getName();
        String poke2Name = battleSim.get(0).getPokemon(1).getName();
        long poke1Level = battleSim.get(0).getPokemon(0).getLevel();
        long poke2Level = battleSim.get(0).getPokemon(1).getLevel();
        // System.out.println(jsonP1);

        String simBattleName = "Test1";

        // Start connection
        SqlHelper db = new SqlHelper();
        db.openConnection();

        db.insertPokemon(poke1Name, (long) jsonP1.get("hp"), (long) jsonP1.get("attack"),
                (long) jsonP1.get("defense"), (long) jsonP1.get("special-attack"), (long) jsonP1.get("special-defense"),
                (long) jsonP1.get("speed"), poke1Level);
        db.insertPokemon(poke2Name,
                (long) jsonP2.get("hp"),
                (long) jsonP2.get("attack"), (long) jsonP2.get("defense"), (long) jsonP2.get("special-attack"),
                (long) jsonP2.get("special-defense"), (long) jsonP2.get("speed"),
                poke2Level);
        // DO A TRY CATCH WHEN ITS -1
        db.insertSimulation(simBattleName,
                db.getPokeId(poke1Name, poke1Level),
                db.getPokeId(poke2Name, poke2Level));

        db.closeConnection();
    }

    public void exportResultsToDB() {
        ;
    }
}
