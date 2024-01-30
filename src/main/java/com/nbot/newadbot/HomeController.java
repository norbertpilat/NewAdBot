package com.nbot.newadbot;

import org.quartz.SchedulerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
public class HomeController{
    private final NewAdBotService newAdBotService;
    private final UserRepository userRepository;

    public HomeController(NewAdBotService newAdBotService, UserRepository userRepository) {
        this.newAdBotService = newAdBotService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    String home(){
        checkForNewDataForAllUsers();
        return "hejka";
    }


    public void checkForNewDataForAllUsers() {
        List<User> users = userRepository.getAllUsers();
        for (User user : users) {
            try {
                ScheduleTaskNewData.starScheduler(newAdBotService,user);
                // Oczekaj zanim przejdziesz do następnego użytkownika
                Thread.sleep(TimeUnit.SECONDS.toMillis(50));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
