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
    @ElementCollection
    private Set<String> urlSet;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Links() {
    }

    public Links(User user,Set<String> urlSet) {
        this.urlSet = urlSet;
        this.user = user;
    }
}
