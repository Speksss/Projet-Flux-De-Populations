package application.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.json.JSONObject;

import javax.persistence.*;
import java.time.DayOfWeek;

@Entity
public class Schedule {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Integer id;

    @Column(length = 5000)
    private String data;

    public static String buildBasicJson(int value) {
        JSONObject jsonObject = new JSONObject();
        for(DayOfWeek day : DayOfWeek.values()) {
            JSONObject jsonDay = new JSONObject();
            for(int hour = 0; hour < 24; hour++) {
                jsonDay.put(String.valueOf(hour), value);
            }
            jsonObject.put(day.name(), jsonDay);
        }

        return jsonObject.toString();
    }

    public String getJsonDay(DayOfWeek dayOfWeek) {
        if(data == null || data.equals("")) return "";
        JSONObject jsonObject = new JSONObject(data);
        return jsonObject.optString(dayOfWeek.name(), "");
    }

    public int getCount(DayOfWeek dayOfWeek, int hour) {
        if(data == null || data.equals("")) return 0;
        JSONObject jsonObject = new JSONObject(data);
        if(!jsonObject.has(dayOfWeek.name())) return 0;
        JSONObject day = jsonObject.getJSONObject(dayOfWeek.name());
        return day.optInt(String.valueOf(hour), 0);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
