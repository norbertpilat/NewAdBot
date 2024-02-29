package com.nbot.newadbot;

import org.springframework.data.repository.CrudRepository;
import java.util.Optional;


public interface LinksRepository extends CrudRepository<Links,Long> {
    Optional<Links> findByUser(User user);
}
