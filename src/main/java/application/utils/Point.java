package application.utils;

import java.lang.Math;


/**
 * Objet Point permet de faciliter le traitement
 * des données relatives aux localisations
 */
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


    /**
     * Calcule l'aire du triangle forme par les 3 points
     */
    public static double getAreaTriangle(Point p1, Point p2, Point p3){

        return Math.abs((p1.getX()*(p2.getY()-p3.getY()) +
                        p2.getX()*(p3.getY()-p1.getY()) +
                        p3.getX()*(p1.getY()-p2.getY())) / 2);

    }

}
