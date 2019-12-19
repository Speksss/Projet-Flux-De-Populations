package application.service;

import application.entity.Area;
import application.entity.Capteur;
import application.repository.CapteurRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Services liés aux capteurs
 */
@Service
public class CapteurService {

    private static final Logger log = LoggerFactory.getLogger(CapteurService.class);

    @Autowired
    CapteurRepository capteurRepository;

    @Autowired
    AreaService areaService;

    //CREATE UPDATE

    /**
     * Sauvegarde d'un capteur
     * @param c Capteur à sauvegarder
     * @return True / False
     */
    public Capteur saveCapteur(Capteur c){
        return capteurRepository.save(c);
    }

    //GET

    /**
     * Recherche d'un capteur par son id
     * @param id Id du capteur
     * @return Capteur ou NULL
     */
    public Capteur getCapteurById(String id){
        return capteurRepository.findById(id);
    }

    /**
     * Retourne la liste de tout les capteurs
     * @return Liste de capteurs ou NULL
     */
    public List<Capteur> getAll(){
        return capteurRepository.findAll();
    }

    /**
     * Modifie un capteur
     * @param c Capteur à modifier
     * @return True / False
     */
    public Capteur update(Capteur c){
        return this.capteurRepository.save(c);
    }

    /**
     * Supprime un capteur
     * @param c Capteur a supprimer
     */
    public void delete(Capteur c){
        this.capteurRepository.delete(c);
    }

    /**
     * Retourne la liste des capteurs présents sur une zone
     * @param a Zone ciblée
     * @return List de capteurs
     */
    public List<Capteur> getCapteurByArea(Area a){
        List<Capteur> capteurList = this.getAll();
        List<Capteur> res = new ArrayList<>();
        for(Capteur c : capteurList){
            JSONObject capteurData = new JSONObject(c.getDatas());
            for(String nomCapteur : capteurData.keySet()) {
                JSONObject data = capteurData.getJSONObject(nomCapteur);
                double latitude = Double.parseDouble((String)data.get("latitude"));
                double longitude = Double.parseDouble((String)data.get("longitude"));
                List<Area> areaList = areaService.findAreasByCoordinates(latitude,longitude);
                if(areaList.contains(a)){
                    res.add(c);
                    break;
                }
            }
        }
        return res;
    }

}
