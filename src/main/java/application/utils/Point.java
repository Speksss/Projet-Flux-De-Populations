package application.utils;

import java.lang.Math;


public class Point {

    private final double x;
    private final double y;


    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    private double squarePoint(Point p){

        return (Math.pow((p.getX() - this.x),2) + (Math.pow(p.getY() - this.y,2)));
    }

    /**
     * Calcule le produit scalaire entre 2 vecteurs composes des points donnees
     * Exemple AB.AC (A p1, B p2, C p3)
     */
    public static double scalarProduct(Point p1, Point p2, Point p3){

        return 0.5 * (p1.squarePoint(p2) + p1.squarePoint(p3) - p2.squarePoint(p3));

    }

    /**
     * Calcule l'aire du triangle forme par les 3 points
     */
    public static double getAreaTriangle(Point p1, Point p2, Point p3){
        return Math.abs((p1.getX()*(p2.getY()-p3.getY()) +
                p2.getX()*(p3.getY()-p1.getY() +
                        p3.getX()*(p1.getY()-p2.getY()))) / 2);

    }

}
