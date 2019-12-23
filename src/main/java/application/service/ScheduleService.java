package application.service;

import application.entity.Area;
import application.entity.Schedule;
import application.repository.ScheduleRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleService {

    private static final Logger log = LoggerFactory.getLogger(ScheduleService.class);

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AreaService areaService;

    private LocalDateTime lastUpdate = LocalDateTime.now();

    private int                   updateCount    = 0;
    private Map<Integer, Integer> areaUsersCount = new HashMap<>();

    public Schedule saveSchedule(Schedule schedule) {
        return this.scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Schedule schedule) {
        this.scheduleRepository.delete(schedule);
    }

    public List<Schedule> findAll() {
        return this.scheduleRepository.findAll();
    }

    public Map<Integer, Schedule> findAllByAreaIds() {
        List<Area>             areas  = this.areaService.findAllAreas();
        Map<Integer, Schedule> result = new HashMap<>();
        for(Area area : areas) {
            if(area.getSchedule() != null) {
                result.put(area.getId(), area.getSchedule());
            }
        }

        return result;
    }

    @Scheduled(fixedRate = 300000)
    private void localUpdate() {
        log.info("Mise à jour des utilisateurs par zone, lastUpdate = {}", lastUpdate);
        LocalDateTime now = LocalDateTime.now();
        if(now.getDayOfWeek().equals(DayOfWeek.SATURDAY) || now.getDayOfWeek().equals(DayOfWeek.SUNDAY)) return;
        if(!lastUpdate.getMonth().equals(now.getMonth()) || !lastUpdate
                .getDayOfWeek()
                .equals(now.getDayOfWeek()) || lastUpdate.getHour() != now.getHour() || updateCount > 3)
        {
            this.updateDatabase();
        }
        List<Area>            areas            = areaService.findAllAreas();
        Map<Integer, Integer> countUsersByArea = userService.countAllUsersByAreaId();
        for(Area area : areas) {
            int areaId = area.getId();
            areaUsersCount.putIfAbsent(areaId, 0);
            Integer count = 0;
            if((count = countUsersByArea.get(areaId)) != null) {
                areaUsersCount.put(areaId, areaUsersCount.get(areaId) + count);
            }
        }
        this.updateCount++;
        this.lastUpdate = now;
    }

    private void updateDatabase() {
        log.info("[UPDATE] Schedule");
        DayOfWeek  day   = lastUpdate.getDayOfWeek();
        int        hour  = lastUpdate.getHour();
        List<Area> areas = this.areaService.findAllAreas();
        for(Area area : areas) {
            Schedule schedule;
            if((schedule = area.getSchedule()) == null) continue;
            JSONObject json     = new JSONObject(schedule.getData());
            JSONObject jsonDay  = json.getJSONObject(day.name());
            int        oldCount = jsonDay.getInt(String.valueOf(hour));
            int        count    = areaUsersCount.get(area.getId()) / (updateCount == 0 ? 1 : updateCount);
            if(oldCount == 0) {
                jsonDay.put(String.valueOf(hour), count);
            } else {
                jsonDay.put(String.valueOf(hour), (oldCount + count) / 2);
            }
            schedule.setData(json.toString());
            this.saveSchedule(schedule);
        }
        updateCount = 0;
        areaUsersCount.clear();
    }

    public Schedule findScheduleByAreaId(Integer areaId) {
        Area area = this.areaService.findAreaById(areaId);
        return area == null ? null : area.getSchedule();
    }
}
