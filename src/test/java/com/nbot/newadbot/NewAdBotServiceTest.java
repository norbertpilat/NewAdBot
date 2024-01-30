package com.nbot.newadbot;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
