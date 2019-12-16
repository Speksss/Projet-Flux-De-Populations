package application.service;

import application.entity.Area;
import application.utils.Point;
import org.json.JSONObject;

/**
 * Services liés aux zones
 */
public class AreaService {


    /**
     * Fonction qui verifie si un point est dans une zone specifiee
     * @param area = zone
     * @param p = point
     * @return true si le point est dans la zone, false sinon
     */
   public boolean isPointInArea(Area area, Point p){

       JSONObject obj = new JSONObject(area.getCoordinates());
       Point a = new Point(obj.getDouble("x1") ,obj.getDouble("y1"));
       Point b = new Point(obj.getDouble("x2") ,obj.getDouble("y2"));
       Point c = new Point(obj.getDouble("x3") ,obj.getDouble("y3"));
       Point d = new Point(obj.getDouble("x4") ,obj.getDouble("y4"));
       // a    d
       //    p
       // b    c

       // Si la somme des aires des triangles reliant le point est egale a
       // l'aire du quadrilatere, alors le point est dans la zone
       double total_area = Point.getAreaTriangle(a, b, c)
               + Point.getAreaTriangle(a, d, c);

       double sum_area = Point.getAreaTriangle(a, b, p) + Point.getAreaTriangle(b, c, p)
               + Point.getAreaTriangle(c, d, p) + Point.getAreaTriangle(d, a, p);

       return (sum_area == total_area);
   }






}
