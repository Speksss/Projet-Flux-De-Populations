package com.front.entity;



public class Capteur {

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

    @Override
    public String toString() {
        return "Capteur [id=" + id + ", datas=" + datas + "]";
    }


}