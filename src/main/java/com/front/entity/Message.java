package com.front.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.context.annotation.Scope;

import java.text.SimpleDateFormat;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G HH:mm:ss");

    private String date;
    private Integer id;
    private String messageBody;
    private User transmitter;

    public Message(Integer creationTimestamp, Integer id, String messageBody, User transmitter) {
        this.date = sdf.format(creationTimestamp);
        this.id = id;
        this.messageBody = messageBody;
        this.transmitter = transmitter;
    }

    public Message(Integer id, String messageBody, User transmitter) {
        this.id = id;
        this.messageBody = messageBody;
        this.transmitter = transmitter;
    }

    public Message() {}


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public User getTransmitter() {
        return transmitter;
    }

    public void setTransmitter(User transmitter) {
        this.transmitter = transmitter;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String creationTimestamp) {
        this.date = creationTimestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "creationTimestamp=" + date +
                ", id=" + id +
                ", messageBody='" + messageBody + '\'' +
                ", transmitter=" + transmitter +
                '}';
    }
}
