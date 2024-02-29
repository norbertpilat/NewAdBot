package com.nbot.newadbot;

import com.nbot.newadbot.config.BotConfig;
import com.nbot.newadbot.quartzjob.ScheduleTaskNewData;
import com.nbot.newadbot.user.User;
import com.nbot.newadbot.user.UserService;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class TelegramBotService extends TelegramLongPollingBot
{
    private static final String START_TEXT = "Witaj w Poszukiwaczu Aukcji na OLX! \nPodaj link do obserwacji :) \n" +
            "Jeśli chcesz przerwać wysyłanie wiadomości wyślij /stop";
    private final BotConfig botConfig;
    private final UserService userService;
    private final NewAdBotService newAdBotService;

    @Override public String getBotUsername()
    {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override public void onUpdateReceived( Update update )
    {
        try
        {
            onSendLinks(update);

        }
        catch (TelegramApiException | SchedulerException e )
        {
            throw new RuntimeException( e );
        }
    }

    private void onSendLinks( Update update )
        throws TelegramApiException, SchedulerException
    {
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();
        if ( userService.userIsExist( chatId ) && text.contains( "www.olx" )) {
            String link = update.getMessage().getText();
            userService.editUser(link,chatId);
            User userByChatId = userService.getUserByChatId( chatId );
            ScheduleTaskNewData.starScheduler( newAdBotService, userByChatId );
            execute(replyToLink(chatId));
        }else if (userService.userIsExist(chatId) && text.contains("/stop ")) {
            userService.deleteUser(chatId);
        } else if (userService.userIsExist(chatId) && !text.contains("www.olx")) {
            execute(replyToUnrecognizedMessage(chatId));
        }  else {
            execute( replyToStart( chatId ) );
            userService.adNewUser( chatId, "link",30);
        }
    }
    public SendMessage replyToStart(long chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId( chatId );
        sendMessage.setText(START_TEXT);
        return sendMessage;
    }

    public SendMessage replyToUnrecognizedMessage(long chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId( chatId );
        sendMessage.setText("Żądanie nie rozpoznane \uD83D\uDE1E");
        return sendMessage;
    }

    public SendMessage replyToLink(long chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId( chatId );
        sendMessage.setText("Link został dodany do obserwowania," +
                " domyślny czas odświeżania wyszukiwania to 10 minut.\nUdanego poszukiwania okazji! \uD83E\uDD2A");
        return sendMessage;
    }
}



