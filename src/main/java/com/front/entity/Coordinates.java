package com.front.entity;

public class Coordinates {

    private double X1;
    private double X2;
    private double X3;
    private double X4;
    private double Y1;
    private double Y2;
    private double Y3;
    private double Y4;

    public Coordinates(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        X1 = x1;
        X2 = x2;
        X3 = x3;
        X4 = x4;
        Y1 = y1;
        Y2 = y2;
        Y3 = y3;
        Y4 = y4;
    }

    public Coordinates() {}

    public double getX1() {
        return X1;
    }

    public void setX1(double x1) {
        X1 = x1;
    }

    public double getX2() {
        return X2;
    }

    public void setX2(double x2) {
        X2 = x2;
    }

    public double getX3() {
        return X3;
    }

    public void setX3(double x3) {
        X3 = x3;
    }

    public double getX4() {
        return X4;
    }

    public void setX4(double x4) {
        X4 = x4;
    }

    public double getY1() {
        return Y1;
    }

    public void setY1(double y1) {
        Y1 = y1;
    }

    public double getY2() {
        return Y2;
    }

    public void setY2(double y2) {
        Y2 = y2;
    }

    public double getY3() {
        return Y3;
    }

    public void setY3(double y3) {
        Y3 = y3;
    }

    public double getY4() {
        return Y4;
    }

    public void setY4(double y4) {
        Y4 = y4;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "X1=" + X1 +
                ", X2=" + X2 +
                ", X3=" + X3 +
                ", X4=" + X4 +
                ", Y1=" + Y1 +
                ", Y2=" + Y2 +
                ", Y3=" + Y3 +
                ", Y4=" + Y4 +
                '}';
    }
}
