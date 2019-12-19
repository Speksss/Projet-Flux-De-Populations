package com.front.controller;

import com.front.Main;
import com.front.entity.Message;
import com.front.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
public class ApplicationController {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private List<User> listUser = new ArrayList<>();
    private List<Message> listMessage = new ArrayList<>();

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {

        if(checkSessionTokenValidity(request)){ // Verify Connection
            panelModel(model);
            model.addAttribute("header", "panel");
            return "index";
        }
        else{
            model.addAttribute("User", new User());
            return "login";
        }
    }

    @RequestMapping(value = "/application", method = RequestMethod.POST)
    public String header(@RequestParam("select") String selectHeader, Model model, HttpServletRequest request){

        if(checkSessionTokenValidity(request)){
            if(selectHeader.equals("panel")){
                panelModel(model);
                model.addAttribute("header", "panel");}
            if(selectHeader.equals("capteurs")){ model.addAttribute("header", "capteurs");}
            if(selectHeader.equals("utilisateurs")){
                AllUser(model);
                model.addAttribute("header", "utilisateurs");
            }
            if(selectHeader.equals("evenements")){ model.addAttribute("header", "evenements");}
            if(selectHeader.equals("messages")){
                AllMesssage(model, request);
                model.addAttribute("header", "messages");
            }

            return "index";
        }
        else{
            model.addAttribute("User", new User());
            return "login";
        }

    }

    public void panelModel(Model model){

        final String uri = "http://35.206.157.216:8080/user/all";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<User[]> response = restTemplate.getForEntity(uri, User[].class);
        User[] users = response.getBody();

        model.addAttribute("NbUsers", String.valueOf(users.length));
    }

    public void AllMesssage(Model model, HttpServletRequest request){

        String user = request.getSession().getAttribute("user").toString();
        log.info(user);

        final String uri = "http://35.206.157.216:8080/message/all?emailAdmin=" + user;

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Message[]> response =
                //restTemplate.exchange(uri, HttpMethod.GET, entity, new ParameterizedTypeReference<Message[]>(){});
                restTemplate.getForEntity(uri, Message[].class);
        Message[] messages = response.getBody();
        listMessage = Arrays.asList(messages);

        model.addAttribute("listMessage", listMessage);

        log.info(listMessage.toString());

    }

    public void AllUser(Model model){

        final String uri = "http://35.206.157.216:8080/user/all";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<User[]> response = restTemplate.getForEntity(uri, User[].class);
        User[] users = response.getBody();
        listUser = Arrays.asList(users);

        int cpt = 0;
        for(User u : listUser){
            cpt++;
            u.setId(cpt);
        }

        model.addAttribute("listUser", listUser);
        model.addAttribute("User", new User());

        log.info(listUser.toString());

    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public String updateUser(@RequestParam("email") String email,
                             @ModelAttribute User update,
                             HttpServletRequest request,
                             Model model) {

        String user = request.getSession().getAttribute("user").toString();
        log.info(user);

        final String uri = "http://35.206.157.216:8080/admin/user/update?emailAdmin=" + user + "&email=" + email;

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.put(uri, update);

        log.info("User updated");
        model.addAttribute("header", "utilisateurs");
        AllUser(model);

        return "index";

    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public String delete(@RequestParam("email") String email,  HttpServletRequest request, Model model){

        String user = request.getSession().getAttribute("user").toString();
        log.info(user);

        final String uri = "http://35.206.157.216:8080/admin/user/delete?emailAdmin=" + user + "&emailUser=" + email;

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(uri, User.class);
        log.info("User deleted");
        model.addAttribute("header", "utilisateurs");
        AllUser(model);
        return "index";
    }

    public boolean checkSessionTokenValidity(HttpServletRequest request) {
        return (request.getSession().getAttribute("user") != null);
    }
}
