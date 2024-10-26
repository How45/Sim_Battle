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

    public Pokemon(String name, JSONObject stats,
            JSONObject moveSet, int level) {
        this.name = name;
        this.stats = stats;
        this.moveSet = moveSet;
        this.level = level;

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

    public String getName() {
        return this.name;
    }

    public Object getStat(String typeOfStat) {
        return this.stats.get(typeOfStat);
    }

    public JSONObject moveChoice() {
        String m = "{\"PP\": 20,\"Accuracy\": 100,\"Type\":\"electric\",\"Category\":\"special\",\"Priority\":0,\"Power\":40}";
        JSONObject move = (JSONObject) JSONValue.parse(m);
        return move;

        // Set<String> set = this.moveSet.keySet();
        // String nullMove = "{\"Category\": null,\"Accuracy\": null,\"Power\":
        // null,\"PP\": null,\"Priority\": null,\"Type\": null}";
        // JSONObject nullMoveObj = (JSONObject) JSONValue.parse(nullMove);

        // if (set == null || set.isEmpty()) {
        // return nullMoveObj;
        // }

        // int randMove = new Random().nextInt(set.size());

        // int i = 0;
        // for (String m : set) {
        // if (i == randMove) {
        // return (JSONObject) this.moveSet.get(m); // <---- THis case will always
        // happen as we checked if set was
        // // empty
        // }
        // i++;
        // }

        // return nullMoveObj;
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

}
