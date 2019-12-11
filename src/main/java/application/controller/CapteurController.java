package application.controller;

import application.entity.Capteur;
import application.service.CapteurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlleur dédié a la manipulation des capteurs
 */
@RestController
public class CapteurController {
     @Autowired
     CapteurService capteurService;

    private static final Logger log = LoggerFactory.getLogger(CapteurController.class);

    /**
     * Récupération d'un capteur
     * @param id Id du capteur
     * @return Capteur ou null
     */
    @GetMapping("/capteur")
    @ResponseBody
    public Capteur getCapteur(
             @RequestParam(value="id")Integer id
    ){
        System.out.println("[GET] Capteur by id : "+id);
        return capteurService.getCapteurById(id);
    }

    /**
     * Sauvegarde d'un capteur (CREATE and UPDATE)
     * @param nom Nom du capteur
     * @param datas Données liées au capteur
     * @param id Id du capteur (Uniquement si il existe déjà)
     */
    @PostMapping("/capteur")
    @ResponseBody
    public void saveCapteur(
            @RequestParam(value="nom") String nom,
            @RequestParam(value="datas")String datas,
            @RequestParam(value="id",required = false) Integer id
    ){
        Capteur c = (id!=null)? capteurService.getCapteurById(id) : new Capteur();
        c.setDatas(datas);
        c.setNom(nom);
        System.out.println("[SAVE] Capteur : "+c.toString());
        capteurService.saveCapteur(c);
    }

    /**
     * Retoure l'ensemble des capteurs
     * @return List de Capteurs
     */
    @GetMapping("/capteurs")
    @ResponseBody
    public List<Capteur> getAllCapteurs(){
        System.out.println("[GET] All Capteurs");
        return capteurService.getAll();
    }

}
