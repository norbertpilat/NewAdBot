package com.nbot.newadbot;


import com.nbot.newadbot.links.LinksRepository;
import com.nbot.newadbot.user.User;
import com.nbot.newadbot.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;


import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class NewAdBotServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private LinksRepository linksRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @InjectMocks
    private NewAdBotService newAdBotService;

    @Test
    void testCheckForNewData(){

        //given
        long chatId = 123L;
        User user = new User();
        user.setChatId(chatId);
        user.setLink("https://www.olx.pl/elektronika/smartwatche-i-opaski/q-apple-watch/?search%5Border%5D=created_at:desc&search%5Bfilter_enum_state%5D%5B0%5D=damaged");
        user.setTimeRefresh(20);

        userRepository.save(user);

        //when
        Mockito.when(userRepository.getUserByChatId(chatId)).thenReturn(Optional.of(user));
        Mockito.when(linksRepository.existsByUser(user)).thenReturn(true);

        newAdBotService.checkForNewData(chatId);

        //then
        Mockito.verify(userRepository,Mockito.atLeast(1)).save(Mockito.any());
        Mockito.verify(linksRepository, Mockito.times(1)).saveAll(Mockito.any());
        Mockito.verify(eventPublisher, Mockito.atLeast(40)).publishEvent(Mockito.any());
    }
}

