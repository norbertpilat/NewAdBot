package com.nbot.newadbot.user;

import com.nbot.newadbot.links.Links;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private Long chatId;

    private String link;

    @OneToMany( mappedBy = "user" )
    private List<Links> oldData = new ArrayList<>();

    private int timeRefresh;

    private UserStats chatStatus;

    public User()
    {
    }
    public User( Long chatId, String link, int timeRefresh )
    {
        this.chatId = chatId;
        this.link = link;
        this.timeRefresh = timeRefresh;
    }
}