package application.service;

import application.entity.Message;
import application.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    public List<Message> findAll(){return messageRepository.findAll();}

    public Message findById(int id){return messageRepository.findById(id);}

    public void delete(Message message){messageRepository.delete(message);}

    public boolean save(Message message){
        Message m = messageRepository.save(message);
        return (m != null);
    }
}
