package com.tokyomagic.lab3missionarchive.model;

import com.mycompany.lab2missionanalyzerpatterns.model.enums.SorcererRank;

public class Sorcerer {
    private String name;
    private SorcererRank rank;

    public Sorcerer() {}

    public Sorcerer(String name, SorcererRank rank) {
        this.name = name;
        this.rank = rank;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public SorcererRank getRank() { return rank; }
    public void setRank(SorcererRank rank) { this.rank = rank; }

    public void setRankString(String rankStr) {
        this.rank = SorcererRank.fromString(rankStr);
    }
}