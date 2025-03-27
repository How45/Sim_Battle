package com.pokemon;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Pokemon {

    private String name;
    private int hp;
    private JSONObject staticStats, stats;
    private JSONObject moveSet;
    private int level;
    private String effect = null;
    private String type1, type2;
    private int move1PositiveEffect, move2PositiveEffect, move3PositiveEffect;
    private Set<Integer> weights = new HashSet<>();

    public Pokemon(String name, JSONObject stats,
            JSONObject moveSet, int level, String type1, String type2) {
        this.name = name;
        this.staticStats = stats;
        this.stats = stats;
        this.moveSet = moveSet;
        this.level = level;
        this.type1 = type1;
        this.type2 = type2;

        // Set Hp when inizalised
        // Not sure if I need this try catch
        try {
            this.hp = (int) (long) stats.get("hp");
        } catch (Exception e) {
            e.printStackTrace();
            this.hp = 0;
        }

        this.move1PositiveEffect = 100;
        this.move2PositiveEffect = 100;
        this.move3PositiveEffect = 100;

        Map<String, Integer> statDeduction = new HashMap<String, Integer>() {
            {
                put("attack", 0);
                put("defence", 0);
                put("special", 0);
                put("speed", 0);
            }
        };

    }

    public int getHealth() {
        return this.hp;
    }

    public void reduceHealth(int damage) {
        this.hp = this.hp - damage;
    }

    public boolean isDead() {
        return this.hp <= 0 ? true : false;
    }

    public String getName() {
        return this.name;
    }

    public int getLevel() {
        return this.level;
    }

    public String getType1() {
        return this.type1;
    }

    public String getType2() {
        return this.type2;
    }

    public Object getStat(String typeOfStat) {
        return this.stats.get(typeOfStat);
    }

    public Object getAllStats() {
        return this.stats;
    }

    public void resetAll() {
        this.stats = this.staticStats;
        effect = null;
        this.hp = (int) (long) this.staticStats.get("hp");
    }

    public void setWeights(String stance) {
        weights.clear();

        if (stance.equals("aggressive")) {
            weights.add(40); // Physical
            weights.add(40); // Special
            weights.add(20); // Status
        } else if (stance.equals("normal")) {
            weights.add(33); // Physical
            weights.add(33); // Special
            weights.add(33); // Status
        } else {
            weights.add(20); // Physical
            weights.add(20); // Special
            weights.add(60); // Status
        }
    }

    public JSONObject moveChoice() {
        Set<String> set = this.moveSet.keySet();
        String nullMove = "{\"Category\": null,\"Accuracy\": null,\"Power\":null,\"PP\": null,\"Priority\": null,\"Type\": null}";
        JSONObject nullMoveObj = (JSONObject) JSONValue.parse(nullMove);

        if (set != null || !set.isEmpty()) {

            int randMove = new Random().nextInt(set.size());

            int i = 0;
            for (String m : set) {

                if (i == randMove) {
                    return (JSONObject) this.moveSet.get(m); // <---- This case will always be
                }

                i++;
            }
        }

        return nullMoveObj;
    }

    public double getCritChance() {
        // This is for the calculations of the critical chance in the damage formula
        int speed;

        // Not sure if this try catch is needed
        try {
            speed = (int) (long) this.stats.get("speed");
        } catch (Exception e) {
            e.printStackTrace();
            speed = 0;
        }

        double critChance = (speed * this.level) / 512;
        return Math.min(critChance, 1.0);
    }

    public void setStatusEffect(String typeEffect) {
        this.effect = typeEffect;
    }

    public double sameTypeAttackBase(String moveType) {
        double typeEffectivenessType1 = moveType.equals(this.type1) ? 1.5 : 1;
        double typeEffectivenessType2 = moveType.equals(this.type2) ? 1.5 : 1;

        return typeEffectivenessType1 > typeEffectivenessType2 || typeEffectivenessType1 == typeEffectivenessType2
                ? typeEffectivenessType1
                : typeEffectivenessType2;
    }

}
