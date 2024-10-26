package com.pokemon;

import java.util.Random;

public class Simulate {

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
        damage *= type1;
        damage *= type2;

        if (damage == 1) {
            return 1;
        } else {
            damage = damage * randomModif <= 1 ? 1 : damage * randomModif;
            return damage;
        }
    }
}
