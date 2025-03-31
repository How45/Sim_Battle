package com.pokemon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Game {
    private int roundNum = 1;
    private Pokemon pokemon1;
    private Pokemon pokemon2;
    private List<List<HashMap<String, Object>>> rounds;

    public Game(Pokemon pokemon1, Pokemon pokemon2) {
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

    public int getSizeRounds() {
        return this.rounds.size();
    }

    public List<HashMap<String, Object>> aRound(int index) {
        return this.rounds.get(index);
    }

    public Pokemon getPokemon(int n) {
        if (n == 1) {
            return pokemon1;
        }
        return pokemon2;
    }
}