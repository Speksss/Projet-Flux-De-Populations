package application.entity;

import application.utils.Point;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Objects;

/**
 * Entité Zone
 */
@Entity
@Table(name = "area")
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="area_id")
    private Integer id;

    private Integer capacity;

    private String name;

    private String coordinates; // Format json

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoordinates() { return coordinates; }

    public void setCoordinates(String coordinates) { this.coordinates = coordinates; }

    public Integer getCapacity() { return capacity; }

    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    /**
     * Retourne l'aire de la zone
     * @return l'aire de la zone
     */
    public Double getAreaArea(){
        JSONObject obj = new JSONObject(this.getCoordinates());
        Point a = new Point(obj.getDouble("x1") ,obj.getDouble("y1"));
        Point b = new Point(obj.getDouble("x2") ,obj.getDouble("y2"));
        Point c = new Point(obj.getDouble("x3") ,obj.getDouble("y3"));
        Point d = new Point(obj.getDouble("x4") ,obj.getDouble("y4"));

        return Area.getAreaArea(a,b,c,d);



    }

    /**
     * Retourne l'aire de la zone definie par les 4 points
     * a  d
     * b  c
     * @param a Point a
     * @param b Point b
     * @param c Point c
     * @param d Point d
     * @return l'aire de la zone
     */
    public static Double getAreaArea(Point a, Point b, Point c, Point d){

        return (Double)(Point.getAreaTriangle(a, b, c)
                + Point.getAreaTriangle(a, d, c));
    }

    /*
    * Vérifier l'égalité des id et name d'après l'objet
    * @param o Objet o
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Area area = (Area) o;
        return Objects.equals(id, area.id) &&
                Objects.equals(name, area.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
