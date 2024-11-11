package com.pokemon;

import java.util.Random;

// import org.json.simple.JSONObject;

public class Helper {
    private String[] strengthElements = { "normal", "fire", "water", "electric", "grass", "grass", "ice", "fighting",
            "poison", "ground", "flying", "psychic", "bug", "rock", "ghost", "dragon" };
    private double[][] strenghtChart = {
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

    public int damageFormula(int level, int power, int attackStat, int defenceStat, double type1,
            double type2, double critChance, double stab) {
        // Random number to be 1 or 0
        Random random = new Random();
        double critRandom = random.nextDouble();
        int randomModif = (random.nextInt(255 - 217) + 255) / 255;

        // Crit chance is calculated based on the speed of pokemon
        int cirtical = (critRandom < critChance) ? 1 : 0;

        int damage = ((((2 * level * cirtical) / 5) + 2) * power * (attackStat / defenceStat)) / 50;

        // Damage thus far i
        int sameAttackDamage = (int) (damage * stab) / 2;

        damage *= sameAttackDamage;
        if (type1 != 0) {
            damage *= type1;
        }
        if (type2 != 0) {
            damage *= type2;
        }

        if (damage == 1) {
            return 1;
        } else {
            damage = damage * randomModif <= 1 ? 1 : damage * randomModif;
            return damage;
        }
    }

    public double effectiveTypeAgainst(String attackType, String defenceType) {
        int indexAttackType = -1;
        int indexDefenceType = -1;

        if (defenceType == null) {
            return 0;
        }

        for (int i = 0; i < strengthElements.length; i++) {
            if (strengthElements[i] == attackType) {
                indexAttackType = i;
            }
            if (strengthElements[i] == defenceType) {
                indexDefenceType = i;
            }
        }

        if (indexAttackType == -1 || indexDefenceType == -1) {
            return -1;
        }

        return strenghtChart[indexAttackType][indexDefenceType];
    }
}
