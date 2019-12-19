package application.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Entité message
 */
@Entity
@Table(name="message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="message_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User transmitter;

    private String messageBody;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date date;

    public Message(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getTransmitter() {
        return transmitter;
    }

    public void setTransmitter(User transmitter) {
        this.transmitter = transmitter;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Date getDate() {return date;}

    public void setDate(Date date) {this.date = date;}
}
