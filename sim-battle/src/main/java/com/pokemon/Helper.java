package com.pokemon;

import java.util.Random;

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

    public int damageFormula(int level, int power, int attackStat, int defenseStat, double type1,
            double type2, double critChance, double stab) {
        Random random = new Random();
        double critRandom = random.nextDouble();
        double randomModif = (217 + random.nextInt(39)) / 255.0; // Random between 217 and 255, divided by 255

        int critical = (critRandom < critChance) ? 2 : 1;

        // Basic damage
        int damage = (int) ((((2 * level * critical) / 5.0 + 2) * power * (attackStat / defenseStat)) / 50) + 2;

        // Modifier
        damage *= randomModif;
        if (critical == 1) {
            damage *= stab;
        }
        damage *= type1 * type2;

        return Math.max(1, damage);
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
}
