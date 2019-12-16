package application.service;

import application.entity.Area;
import application.repository.AreaRepository;
import application.utils.Point;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AreaService {

    private static final Logger log = LoggerFactory.getLogger(AreaService.class);

    @Autowired
    private AreaRepository areaRepository;

    public void saveNewArea(Area area){

        areaRepository.save(area);
        log.info("saveNewArea() : {}", area.toString());
    }

    public Area findAreaById(long id){
        return this.areaRepository.findById(id);
    }

    public Area findAreaByName(String name){
        return this.areaRepository.findByName(name);
    }

    /**
     * Renvoie la liste des zones dans lesquels le point se situe
     */
    public List<Area> findAreasByCoordinates(double x, double y){
        List<Area> allAreas = this.areaRepository.findAll();

        System.out.println(allAreas);
        Point p = new Point(x,y);
        List<Area> goodAreas = new ArrayList<>();

        for(Area a : allAreas){
            if(isPointInArea(a,p))
                goodAreas.add(a);
            else
                System.out.println(a.getCoordinates() + "\n" + p.getX() + "," + p.getY());
        }
        System.out.println(goodAreas);
        return goodAreas;
    }

    public List<Area> findAllAreas(){
        return this.areaRepository.findAll();
    }


    /**
     * Fonction qui verifie si un point est dans une zone specifiee
     * @param area = zone
     * @param p = point
     * @return true si le point est dans la zone, false sinon
     */
   public static boolean isPointInArea(Area area, Point p){

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

       // TODO: voir si y'a une maniere + clean d'arrondir les decimales
       DecimalFormat df = new DecimalFormat("0.0000000000000000");

       try {
           total_area = (Double)df.parse(df.format(total_area));
           sum_area = (Double)df.parse(df.format(sum_area));
       } catch (ParseException e) {
           e.printStackTrace();
       }
       return (sum_area == total_area);
   }

}
