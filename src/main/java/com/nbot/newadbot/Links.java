package com.nbot.newadbot;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Links {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Links() {
    }

    public Links(User user, String url) {
        this.url = url;
        this.user = user;
    }
}
