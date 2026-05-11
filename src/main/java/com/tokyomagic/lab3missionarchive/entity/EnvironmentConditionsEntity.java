package com.tokyomagic.lab3missionarchive.entity;

import com.tokyomagic.lab3missionarchive.model.enums.Visibility;
import jakarta.persistence.*;

@Entity
@Table(name = "environment_conditions")
public class EnvironmentConditionsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String weather;
    private String timeOfDay;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    private Integer cursedEnergyDensity;

    public EnvironmentConditionsEntity() {}

    // геттеры/сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getWeather() { return weather; }
    public void setWeather(String weather) { this.weather = weather; }
    public String getTimeOfDay() { return timeOfDay; }
    public void setTimeOfDay(String timeOfDay) { this.timeOfDay = timeOfDay; }
    public Visibility getVisibility() { return visibility; }
    public void setVisibility(Visibility visibility) { this.visibility = visibility; }
    public Integer getCursedEnergyDensity() { return cursedEnergyDensity; }
    public void setCursedEnergyDensity(Integer cursedEnergyDensity) { this.cursedEnergyDensity = cursedEnergyDensity; }
}
