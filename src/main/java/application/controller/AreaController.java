package application.controller;

import application.entity.Area;
import application.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controleur dediee a la manipulation de zone
 */
@RestController
public class AreaController {

    @Autowired
    private AreaService areaService;

    private static final Logger log = LoggerFactory.getLogger(AreaController.class);

    /**
     * Récupération d'une zone id
     * @param id Id de la zone
     * @return Area ou null
     */
    @GetMapping("/area/id")
    @ResponseBody
    public Area getAreaById(
            @RequestParam(value="id")Integer id
    ){
        System.out.println("[GET] Area by id : "+id);
        return areaService.findAreaById(id);
    }

    /**
     * Récupération d'une zone par nom
     * @param name Nom de la zone
     * @return Area ou null
     */
    @GetMapping("/area/name")
    @ResponseBody
    public Area getAreaByName(
            @RequestParam(value="name")String name
    ){
        System.out.println("[GET] Area by name : "+name);
        return areaService.findAreaByName(name);
    }


    /**
     * Récupération d'une zone par coordonnees
     * @param x Longitude du point
     * @param y Latitude du point
     * @return Area ou null
     */
    @GetMapping("/area/coordinates")
    @ResponseBody
    public List<Area> getAreaByCoordinates(
            @RequestParam(value="x")double x,
            @RequestParam(value="y")double y
    ){
        System.out.println("[GET] Area by coordinates : "+x+","+y);
        return areaService.findAreasByCoordinates(x,y);
    }




    /**
     * Sauvegarde d'une zone (CREATE and UPDATE)
     *      * @param name Nom de la zone
     *      * @param datas Données liées au capteur
     *      * @param id Id du capteur (Uniquement si il existe déjà)
     *
     * @param name Nom de la zone
     * @param size Nombre de personne max dans la zone
     * @param x1 longitude1
     * @param y1 latitude1
     * @param x2 longitude2
     * @param y2 latitude2
     * @param x3 longitude3
     * @param y3 latitude3
     * @param x4 longitude4
     * @param y4 latitude4
     * @param id Id de la zone (Uniquement si il existe déjà)
     */
    @PostMapping("/area")
    @ResponseBody
    public void saveArea(
            @RequestParam(value="name") String name,
            @RequestParam(value="size") Integer size,
            @RequestParam(value="x1") double x1,
            @RequestParam(value="y1") double y1,
            @RequestParam(value="x2") double x2,
            @RequestParam(value="y2") double y2,
            @RequestParam(value="x3") double x3,
            @RequestParam(value="y3") double y3,
            @RequestParam(value="x4") double x4,
            @RequestParam(value="y4") double y4,
            @RequestParam(value="id",required = false) Integer id
    ){
        Area a = (id!=null)? areaService.findAreaById(id) : new Area();
        String coordinates = String.format("{\"x1\":\"%s\",\"y1\":\"%s\",\"x2\":\"%s\",\"y2\":\"%s\",\"x3\":\"%s\",\"y3\":\"%s\",\"x4\":\"%s\",\"y4\":\"%s\"}",
                x1,y1,x2,y2,x3,y3,x4,y4);
        a.setCoordinates(coordinates);
        a.setName(name);
        a.setSize(size);
        System.out.println("[SAVE] Area : "+a.toString());
        areaService.saveNewArea(a);
    }

    /**
     * Retoure l'ensemble des capteurs
     * @return List de Capteurs
     */
    @GetMapping("/area/all")
    @ResponseBody
    public List<Area> getAllAreas(){
        System.out.println("[GET] All Areas");
        return areaService.findAllAreas();
    }


}
