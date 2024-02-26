package com.nbot.newadbot.links;

import com.nbot.newadbot.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface LinksRepository extends CrudRepository<Links,Long> {
    Optional<Links> findByUser( User user);

    List<Links> findAllUrlsByUserId(Long userId);

    @Query("SELECT l.url FROM Links l WHERE l.user.id = :userId ORDER BY l.id DESC")
    String findLatestUrlByUserId(Long userId);

}
