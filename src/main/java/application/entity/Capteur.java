package application.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * Entité Capteur
 */
@Entity
@Table(name="Capteur")
public class Capteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="capteur_id")
    private Integer id;
    private String nom;
    private String datas;//Données au format JSON

    public Capteur() {}

    public Capteur(Integer _id,String _nom, String _datas){
        this.id = _id;
        this.nom = _nom;
        this.datas = _datas;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }
}
