package application.service;

import application.entity.Area;
import application.entity.Schedule;
import application.repository.AreaRepository;
import application.utils.Point;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AreaService {

    private static final Logger log = LoggerFactory.getLogger(AreaService.class);

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private ScheduleService scheduleService;

    /**
     * Sauvegarde d'une zone
     * @param area : zone à sauvegarder
     * @return True si la zone a bien été sauvegardée, false sinon
     */
    public Area saveNewArea(Area area){
        log.info("saveNewArea() : {}", area.toString());
        Schedule schedule = new Schedule();
        schedule.setData(Schedule.buildBasicJson(area.getCapacity()));
        area.setSchedule(schedule);

        return this.areaRepository.save(area);
    }

    /**
     * Recherche d'une zone par son id
     * @param id : id de la zone
     * @return La zone recherchée, ou null
     */
    public Area findAreaById(Integer id){
        return this.areaRepository.findById(id);
    }

    /**
     * Recherche d'une zone par son nom
     * @param name : nom de la zone
     * @return La zone recherchée, ou null
     */
    public Area findAreaByName(String name){
        return this.areaRepository.findByName(name);
    }

    /**
     * Renvoie la liste des zones dans lesquels le point (donné en entré) se situe
     * @param x : latitude du point
     * @param y : longitude du point
     * @return Liste de zones (peut être vide)
     */
    public List<Area> findAreasByCoordinates(double x, double y){
        List<Area> allAreas = this.areaRepository.findAll();

//        System.out.println(allAreas);
        Point p = new Point(x,y);
        List<Area> goodAreas = new ArrayList<>();

        for(Area a : allAreas){
            if(isPointInArea(a,p))
                goodAreas.add(a);
//            else
//                System.out.println(a.getCoordinates() + "\n" + p.getX() + "," + p.getY());
        }
//        System.out.println(goodAreas);
        return goodAreas;
    }

    /**
     * Recherche la liste de toutes les zones
     * @return Liste des zones
     */
    public List<Area> findAllAreas(){
        return this.areaRepository.findAll();
    }


    /**
     * Supprime une zone
     * @param a Zone a supprimer
     */
    public void delete(Area a){
        this.areaRepository.delete(a);
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
       // a     d
       //    p
       // b     c

       // Si la somme des aires des triangles reliant le point est egale a
       // l'aire du quadrilatere, alors le point est dans la zone
       Double totalArea = Area.getAreaArea(a,b,c,d);

       Double sumArea = (Double)(Point.getAreaTriangle(a, b, p) + Point.getAreaTriangle(b, c, p)
               + Point.getAreaTriangle(c, d, p) + Point.getAreaTriangle(d, a, p));


       double scale = Math.pow(10, 16);
       totalArea = Math.round(totalArea * scale) / scale;
       sumArea = Math.round(sumArea * scale) / scale;

//       DecimalFormat df = new DecimalFormat("0.0000000000000000");
//
//       try {
//           totalArea = (Double)df.parse(df.format(totalArea));
//           sumArea = (Double)df.parse(df.format(sumArea));
//       } catch (ParseException e) {
//           e.printStackTrace();
//       }

       return (sumArea.equals(totalArea));
   }



}
