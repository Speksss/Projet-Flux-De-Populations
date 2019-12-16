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
    @Column(name="capteur_id")
    private String id;
    private String datas;//Données au format JSON

    public Capteur() {}

    public Capteur(String _id, String _datas){
        this.id = _id;
        this.datas = _datas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }
}
