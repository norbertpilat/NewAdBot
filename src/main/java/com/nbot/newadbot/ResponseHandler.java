package com.nbot.newadbot;


import com.nbot.newadbot.links.Links;
import com.nbot.newadbot.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class ResponseHandler implements ApplicationListener<AdEvent>
{
    private final TelegramBotService telegramBotService;

    @Override public void onApplicationEvent( AdEvent event )
    {
        Links links = event.getLinks();
        User user = links.getUser();
        Long chatId = user.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId( chatId );
        sendMessage.setText( links.getUrl());
        try
        {
            telegramBotService.execute( sendMessage );
        }
        catch ( TelegramApiException e )
        {
            throw new RuntimeException( e );
        }
    }
}
