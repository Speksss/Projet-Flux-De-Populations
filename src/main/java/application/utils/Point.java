package application.utils;

import java.lang.Math;


public class Point {

    private double x;
    private double y;


    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
//
//    public double square(){
//        return Math.pow(this.x, 2) + Math.pow(this.y, 2);
//    }

    public double squarePoint(Point p){

        return (Math.pow((p.getX() - this.x),2) + (Math.pow(p.getY() - this.y,2)));
    }

    public static double scalarProduct(Point p1, Point p2, Point p3){

        return 0.5 * (p1.squarePoint(p2) + p1.squarePoint(p3) - p2.squarePoint(p3));

    }

}
