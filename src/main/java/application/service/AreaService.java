package application.service;

import application.entity.Area;
import application.utils.Point;

public class AreaService {

    public boolean isPointInArea(Area area, Point p){


        double x1 = 0;
        double y1 = 0;
        double x2 = 0;
        double y2 = 0;
        double x3 = 0;
        double y3 = 0;
        double x4 = 0;
        double y4 = 0;
        Point a = new Point(x1 ,y1);
        Point b = new Point(x2 ,y2);
        Point c = new Point(x3 ,y3);
        Point d = new Point(x4 ,y4);

        // a(x1,y1)    d(x4,y4)
        //      m(p.x,p.y)
        // b(x2,y2)    c(x3,y3)

        double scalar_ap_ab = Point.scalarProduct(a, p, b);
        double scalar_ab_ab = a.squarePoint(b);


        double scalar_ap_ad = Point.scalarProduct(a, p, d);
        double scalar_ad_ad = a.squarePoint(d);


        if( ((0 < scalar_ap_ab) && (scalar_ap_ab < scalar_ab_ab))  &&
                ((0 < scalar_ap_ad) && (scalar_ap_ad < scalar_ad_ad)))
            return true;
        return false;



    }




}
