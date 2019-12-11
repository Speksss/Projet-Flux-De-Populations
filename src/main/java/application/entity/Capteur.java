package application.entity;

import javax.persistence.*;

/**
 * Entité Capteur
 */
@Entity
@Table(name="Capteur")
public class Capteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private String datas;//Données au format JSON

    public Capteur() {}

    public Capteur(String _nom, String _datas){
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
