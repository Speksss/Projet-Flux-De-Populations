package application.controller;

import application.entity.Capteur;
import application.service.CapteurService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlleur dédié a la manipulation des capteurs
 */
@RestController
@Api(value="fluxDePopulation", description = "Opérations relatives à la gestion basique des capteurs", produces = "application/json")
public class CapteurController {
     @Autowired
     CapteurService capteurService;

    private static final Logger log = LoggerFactory.getLogger(CapteurController.class);

    /**
     * Récupération d'un capteur
     * @param id Id du capteur
     * @return Capteur ou null
     */
    @ApiOperation(value = "Retourne un Capteur", response = Capteur.class)
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
     * @param datas Données liées au capteur
     * @param id Id du capteur (Uniquement si il existe déjà)
     */
    @ApiOperation(value = "Sauvegarde un capteur", response = String.class)
    @PostMapping("/capteur")
    @ResponseBody
    public ResponseEntity<String> saveCapteur(
            @RequestParam(value="id") Integer id,
            @RequestParam(value="datas")String datas
    ){
        Capteur c = new Capteur(id,datas);
        System.out.println("[SAVE] Capteur : "+c.toString());
        if(capteurService.saveCapteur(c))
            return new ResponseEntity<>("Le capteur à été enregistré",HttpStatus.ACCEPTED);
        else
            return new ResponseEntity<>("Une erreur est survenue lors de la sauvegarde du  capteur.", HttpStatus.NOT_MODIFIED);
    }

    /**
     * Retoure l'ensemble des capteurs
     * @return List de Capteurs
     */
    @ApiOperation(value = "Retourne la liste des capteurs", response = List.class)
    @GetMapping("/capteurs")
    @ResponseBody
    public List<Capteur> getAllCapteurs(){
        System.out.println("[GET] All Capteurs");
        return capteurService.getAll();
    }

    /**
     * Modification d'un capteur
     * @param id
     * @param datas
     * @return Le capteur modifié ou null si erreur
     */
    @ApiOperation(value = "Modifie un capteur", response = Capteur.class)
    @PostMapping("/capteur/update")
    @ResponseBody
    public Capteur update(
            @RequestParam(value="id")Integer id,
            @RequestParam(value="datas",required = false)String datas
    ){
        Capteur c = this.capteurService.getCapteurById(id);
        if(c!=null){
            if(datas != null)c.setDatas(datas);
            this.capteurService.update(c);
            return c;
        }
        return null;
    }

    /**
     * Supression d'un capteur
     * @param id Id du capteur à supprimer
     */
    @ApiOperation(value = "Supprime un capteur", response = String.class)
    @DeleteMapping("capteur/delete")
    @ResponseBody
    public ResponseEntity<String> delete(
            @RequestParam(value="id")Integer id
    ){
        Capteur c = this.capteurService.getCapteurById(id);
        if(c != null){
           this.capteurService.delete(c);
           return new ResponseEntity<>("Capteur supprimé",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Capteur introuvable",HttpStatus.NOT_MODIFIED);
        }
    }

}
