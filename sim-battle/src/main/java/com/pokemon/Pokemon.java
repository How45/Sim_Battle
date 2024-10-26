package com.pokemon;

import java.util.Random;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Pokemon {

    private String name;
    private int hp;
    private JSONObject stats;
    private JSONObject moveSet;
    private int level;
    private String effect;
    private String type1;
    private String type2;

    public Pokemon(String name, JSONObject stats,
            JSONObject moveSet, int level, String type1, String type2) {
        this.name = name;
        this.stats = stats;
        this.moveSet = moveSet;
        this.level = level;
        this.effect = null;
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
    }

    public int getHealth() {
        return this.hp;
    }

    public void reduceHealth(int damage) {
        this.hp = this.hp - damage;
    }

    public String getName() {
        return this.name;
    }

    public Object getStat(String typeOfStat) {
        return this.stats.get(typeOfStat);
    }

    public JSONObject moveChoice() {
        // String m = "{\"PP\": 20,\"Accuracy\":
        // 100,\"Type\":\"electric\",\"Category\":\"special\",\"Priority\":0,\"Power\":40}";
        // JSONObject move = (JSONObject) JSONValue.parse(m);
        // return move;

        Set<String> set = this.moveSet.keySet();
        String nullMove = "{\"Category\": null,\"Accuracy\": null,\"Power\":null,\"PP\": null,\"Priority\": null,\"Type\": null}";
        JSONObject nullMoveObj = (JSONObject) JSONValue.parse(nullMove);

        if (set == null || set.isEmpty()) {
            return nullMoveObj;
        }

        int randMove = new Random().nextInt(set.size());

        int i = 0;
        for (String m : set) {

            if (i == randMove) {
                return (JSONObject) this.moveSet.get(m);
            } // <---- THis case will always
            i++;
        }

        return nullMoveObj;
    }

    public int getCritChance() {
        // This is for the caclation of the critical chance in the damage formula
        int speed;

        // Not sure if this try catch is needed
        try {
            speed = (int) (long) this.stats.get("speed");
        } catch (Exception e) {
            e.printStackTrace();
            speed = 0;
        }

        int cal = (speed * this.level) / 512;
        return cal > 1 ? cal : 1;
    }

    public void setStatusEffect(String typeEffect) {
        this.effect = typeEffect;
    }

    public double[] sameTypeAttack(String moveType) {
        double[] typeEffectiveness = new double[2];

        typeEffectiveness[0] = moveType == type1 ? 1.5 : 1;
        typeEffectiveness[1] = moveType == type2 ? 1.5 : 1;

        return typeEffectiveness;
    }

}
