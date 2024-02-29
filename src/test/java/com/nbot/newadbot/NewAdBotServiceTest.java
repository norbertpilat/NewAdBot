package com.nbot.newadbot;

import com.nbot.newadbot.user.User;
import com.nbot.newadbot.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.verify;

@SpringBootTest
public class NewAdBotServiceTest {
    @InjectMocks
    private NewAdBotService newAdBotService;
    @Mock
    private UserRepository userRepository;

    @Test
    public void testExtractedMethod() {
        // Przygotowanie danych testowych
        String chatId = "testChatId";
        User testUser = new User();
        testUser.setChatId(chatId);
        testUser.setLink("testLink");


    }
}
