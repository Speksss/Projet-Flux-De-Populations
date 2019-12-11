package application.service;

import application.entity.Capteur;
import application.repository.CapteurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CapteurService {

    private static final Logger log = LoggerFactory.getLogger(CapteurService.class);

    @Autowired
    CapteurRepository capteurRepository;

    //CREATE UPDATE

    public boolean saveCapteur(Capteur c){
        if(this.getCapteurByNom(c.getNom()) == null) {
            capteurRepository.save(c);
            log.info("[CAPTEUR][SAVE]", c.toString());
            return true;
        }else return false;
    }

    //GET

    /**
     * Recherche d'un capteur par son nom
     * @param nom Nom du capteur
     * @return Capteur ou NULL
     */
    public Capteur getCapteurByNom(String nom){
        return capteurRepository.findByNom(nom);
    }

    /**
     * Recherche d'un capteur par son id
     * @param id Id du capteur
     * @return Capteur ou NULL
     */
    public Capteur getCapteurById(Integer id){
        return capteurRepository.findById(id);
    }

    /**
     * Retourne la liste de tout les capteurs
     * @return Liste de capteurs ou NULL
     */
    public List<Capteur> getAll(){
        return capteurRepository.findAll();
    }
}
