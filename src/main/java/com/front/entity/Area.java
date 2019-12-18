package com.front.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Area {

    private int capacity;
    private String coordinates;
    private Coordinates coordinatesXY;
    private int id;
    private String name;

    public Area(String coordinates, String name) {
        this.coordinates = coordinates;
        this.name = name;
    }

    public Area(int id, Coordinates coordinatesXY, String name) {
        this.id = id;
        this.coordinatesXY = coordinatesXY;
        this.name = name;
    }

    public Area() {}

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Coordinates getCoordinatesXY() {
        return coordinatesXY;
    }

    public void setCoordinatesXY(Coordinates coordinatesXY) {
        this.coordinatesXY = coordinatesXY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Area{" +
                ", id=" + id +
                "capacity=" + capacity +
                ", coordinatesXY=" + coordinatesXY +
                ", name='" + name + '\'' +
                '}';
    }
}
