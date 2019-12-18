package application.controller;

import application.entity.Message;
import application.entity.User;
import application.service.MessageService;
import application.service.UserService;
import application.utils.RoleType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@Api(value="fluxDePopulation",description="Opérations relatives à la gestion des messgaes.",produces="application/json")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    @ApiOperation(value="Retourne la liste des messages",response = List.class)
    @GetMapping("/message/all")
    @ResponseBody
    public ResponseEntity<List<Message>> getAllMessage(
            @RequestParam(value="emailAdmin")String emailAdmin
//            @RequestParam(value="passwordAdmin")String passwordAdmin
    ){
        User admin = userService.findUserByEmail(emailAdmin);
        if(admin.hasRole(RoleType.ROLE_ADMIN)){
            return new ResponseEntity<>(messageService.findAll(),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);
    }

    @ApiOperation(value="Retourne un message",response = Message.class)
    @GetMapping("/message")
    @ResponseBody
    public ResponseEntity<Message> getMessage(
            @RequestParam(value="emailAdmin")String emailAdmin,
//            @RequestParam(value="passwordAdmin")String passwordAdmin,
            @RequestParam(value="messageId")Integer messageId
    ){
        User admin = userService.findUserByEmail(emailAdmin);
        if(admin.hasRole(RoleType.ROLE_ADMIN)) {
            Message message = messageService.findById(messageId);
            if (message != null)
                return new ResponseEntity<>(message, HttpStatus.OK);
            else
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @ApiOperation(value="Supprime un message",response = String.class)
    @DeleteMapping("/message/delete")
    @ResponseBody
    public ResponseEntity<String> deleteMessage(
            @RequestParam(value="emailAdmin")String emailAdmin,
//            @RequestParam(value="passwordAdmin")String passwordAdmin,
            @RequestParam(value="messageId")Integer messageId
    ){
        User admin = userService.findUserByEmail(emailAdmin);
        if(admin.hasRole(RoleType.ROLE_ADMIN)){
            Message message = messageService.findById(messageId);
            if(message != null){
                messageService.delete(message);
                return new ResponseEntity<>("Message supprimé",HttpStatus.ACCEPTED);
            }
            return new ResponseEntity<>("Message introuvable",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Accès refusé",HttpStatus.FORBIDDEN);
    }

    @ApiOperation(value="Création d'un message",response = String.class)
    @PostMapping("/message/new")
    @ResponseBody
    public ResponseEntity<String> newMessage(
            @RequestParam(value="emailTransmitter")String emailTransmitter,
            @RequestParam(value="messageBody")String messageBody
    ){
        User transmitter = userService.findUserByEmail(emailTransmitter);
        if(transmitter!=null){
            Message message = new Message();
            message.setTransmitter(transmitter);
            message.setMessageBody(messageBody);
            message.setCreationTimestamp(new Date().getTime());
            if(messageService.save(message))
                return new ResponseEntity<>("Message envoyé",HttpStatus.CREATED);
            else
                return new ResponseEntity<>("Erreur lors de la création du message",HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>("Utilisateur inconnu",HttpStatus.NOT_FOUND);
    }
}
