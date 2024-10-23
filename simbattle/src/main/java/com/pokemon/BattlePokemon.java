package com.pokemon;

import java.util.Map;
import java.util.HashMap;

public class BattlePokemon {

    private String name;
    private int hp;
    private Map<String, Integer> stats = new HashMap<>();
    private Map<String, Map<String, Object>> moveSet = new HashMap<>();
    private int level;

    public BattlePokemon(String name, Map<String, Integer> stats,
            Map<String, Map<String, Object>> moveSet, Integer level) {
        this.name = name;
        this.stats = stats;
        this.moveSet = moveSet;
        this.level = level;

        // Set Hp when inizalised
        this.hp = stats.get("health");
    }

    public int getHealth() {
        return this.hp;
    }

    public String getName() {
        return this.name;
    }

    public String moveChoice() {
        return "Use this move";
    }

    public int getCritChance() {
        int speed = this.stats.get("speed");

        return (speed * this.level) / 512;
    }

}
