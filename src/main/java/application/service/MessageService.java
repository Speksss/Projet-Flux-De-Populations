package application.service;

import application.entity.Message;
import application.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service lié aux messages
 */
@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    /**
     * Méthode permettant de récupérer l'ensemble des messages
     * @return Une liste de messages
     */
    public List<Message> findAll(){return messageRepository.findAll();}

    /**
     * Méthode permettant de récupérer un message
     * @param id Id du message
     * @return Un message
     */
    public Message findById(int id){return messageRepository.findById(id);}

    /**
     * Méthode permettant de supprimer un message
     * @param message Message à supprimé
     */
    public void delete(Message message){messageRepository.delete(message);}

    /**
     * Méthode permettant de sauvegarder un message
     * @param message Message a sauvegarder
     * @return True / False
     */
    public boolean save(Message message){
        Message m = messageRepository.save(message);
        return (m != null);
    }
}
