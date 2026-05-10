package com.tokyomagic.lab3missionarchive.model;

import com.mycompany.lab2missionanalyzerpatterns.model.enums.Visibility;

public class EnvironmentConditions {
    private String weather;
    private String timeOfDay;
    private Visibility visibility;
    private Integer cursedEnergyDensity;

    public EnvironmentConditions() {}

    public String getWeather() { return weather; }
    public void setWeather(String weather) { this.weather = weather; }

    public String getTimeOfDay() { return timeOfDay; }
    public void setTimeOfDay(String timeOfDay) { this.timeOfDay = timeOfDay; }

    public Visibility getVisibility() { return visibility; }
    public void setVisibility(Visibility visibility) { this.visibility = visibility; }
    public void setVisibilityString(String visibilityStr) {
        this.visibility = Visibility.fromString(visibilityStr);
    }

    public Integer getCursedEnergyDensity() { return cursedEnergyDensity; }
    public void setCursedEnergyDensity(Integer cursedEnergyDensity) { this.cursedEnergyDensity = cursedEnergyDensity; }
}