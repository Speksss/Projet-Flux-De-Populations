package application.controller;

import application.entity.Area;
import application.entity.Schedule;
import application.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlleur lié aux a l'adaptation des habitudes de localisation utilisateurs
 */
@RestController
@Api(value = "fluxDePopulation", description = "Opérations relatives aux habitudes utilisateurs.", produces =
        "application/json")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    private static final Logger log = LoggerFactory.getLogger(ScheduleController.class);

    @GetMapping("/schedule/all")
    @ResponseBody
    @ApiOperation(value = "Affiche l'intégralité des plannings en base")
    public ResponseEntity<List<Schedule>> findAll() {
        return new ResponseEntity<>(scheduleService.findAll(), HttpStatus.OK);
    }

    /**
     * Récupération d'un planning selon sa zone
     *
     * @param areaId Id de la zone
     *
     * @return Le planning, null sinon
     */
    @ApiOperation(value = "Retourne un planning grace à l'id de la zone concernée", response = Area.class)
    @GetMapping("/schedule/area-id")
    @ResponseBody
    public Schedule getScheduleByAreaId(@RequestParam(value = "areaId") Integer areaId) {
        return scheduleService.findScheduleByAreaId(areaId);
    }
}
