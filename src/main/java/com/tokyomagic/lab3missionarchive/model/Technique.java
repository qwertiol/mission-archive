package com.tokyomagic.lab3missionarchive.model;

import com.mycompany.lab2missionanalyzerpatterns.model.enums.TechniqueType;

public class Technique {
    private String name;
    private TechniqueType type;
    private String owner;
    private long damage;

    public Technique() {}

    public Technique(String name, TechniqueType type, String owner, long damage) {
        this.name = name;
        this.type = type;
        this.owner = owner;
        this.damage = damage;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public TechniqueType getType() { return type; }
    public void setType(TechniqueType type) { this.type = type; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public long getDamage() { return damage; }
    
     public void setDamage(Long damage) {
        if (damage < 0) {
                throw new IllegalArgumentException("Damage cost can't be less than 0");
        }
        this.damage = damage; 
    }

    public void setTypeString(String typeStr) {
        this.type = TechniqueType.fromString(typeStr);
    }
}