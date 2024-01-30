package com.nbot.newadbot;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {
    Optional<User> getUserByChatId(String chatId);
    @Query("SELECT u FROM User u")
    List<User> getAllUsers();
}
