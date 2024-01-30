package com.nbot.newadbot;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String chatId;
    private String link;
    @OneToMany(mappedBy = "user")
    private List<Links> oldData = new ArrayList<>();
    private int timeRefresh;

    public User() {
    }

    public User(String chatId, String link, int timeRefresh) {
        this.chatId = chatId;
        this.link = link;
        this.timeRefresh = timeRefresh;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getTimeRefresh() {
        return timeRefresh;
    }

    public void setTimeRefresh(int timeRefresh) {
        this.timeRefresh = timeRefresh;
    }

}
