package com.pokemon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Game {
    private int roundNum = 1;
    private String pokemon1;
    private String pokemon2;
    private List<List<HashMap<String, Object>>> rounds;

    public Game(String pokemon1, String pokemon2) {
        this.pokemon1 = pokemon1;
        this.pokemon2 = pokemon2;
        this.rounds = new ArrayList<>();
    }

    public void addRound(List<HashMap<String, Object>> round) {
        this.rounds.add(round);
        roundNum++;
    }

    public List<List<HashMap<String, Object>>> getAllRounds() {
        return this.rounds;
    }

    public int getNumOfRounds() {
        return this.rounds.size();
    }

    public List<HashMap<String, Object>> aRound(int index) {
        return this.rounds.get(index);
    }
}