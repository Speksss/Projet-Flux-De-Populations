package com.front.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Area {

    private int id;
    private int capacity;
    private int countUser;
    private String coordinates;
    private Coordinates coordinatesXY;
    private String listX;
    private String listY;
    private String name;

    public Area(int capacity, String coordinates, String name) {
        this.capacity = capacity;
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

    public String getListX() {
        return listX;
    }

    public void setListX(String listX) {
        this.listX = listX;
    }

    public String getListY() {
        return listY;
    }

    public void setListY(String listY) {
        this.listY = listY;
    }

    public int getCountUser() {
        return countUser;
    }

    public void setCountUser(int countUser) {
        this.countUser = countUser;
    }

    @Override
    public String toString() {
        return "Area{" +
                ", id=" + id +
                "capacity=" + capacity +
                ", coordinatesXY=" + coordinatesXY +
                ", name='" + name + '\'' +
                ", countUser=" + countUser +
                '}';
    }
}
