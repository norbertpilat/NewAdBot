package com.nbot.newadbot.links;

import com.nbot.newadbot.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface LinksRepository extends CrudRepository<Links,Long> {

    List<Links> findAllUrlsByUserId(Long userId);

    List<Links> findLinksByUserId(Long userId);
    boolean existsByUser(User user);
    Optional<Links> findByUser(User user);
}
