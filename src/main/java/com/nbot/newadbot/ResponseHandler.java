package com.nbot.newadbot;


import com.nbot.newadbot.links.Links;
import com.nbot.newadbot.quartzjob.ScheduleTaskNewData;
import com.nbot.newadbot.user.User;
import com.nbot.newadbot.user.UserService;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;



@Component
@RequiredArgsConstructor
public class ResponseHandler implements ApplicationListener<AdEvent>
{
    private static final String START_TEXT = "Witaj w Poszukiwaczu Aukcji na OLX! \nPodaj link do obserwacji :) ";

    private final KeyboardFactory keyboardFactory;
    private final TelegramBotService telegramBotService;

    private final UserService userService;
    private final NewAdBotService newAdBotService;


//    public void replyToButtons(long chatId, Update update){
//        User user = userService.getUserByChatId( chatId );
//        UserStats chatStatus = user.getChatStatus();
//        if (update.getMessage().getText().equalsIgnoreCase("/stop")) {
//            System.out.println("elo");
//        }
//        switch (chatStatus) {
//            case UserStats.MENU -> replyToWelcome(chatId, message);
//            case UserStats. -> replyToFoodDrinkSelection(chatId, message);
//            case PIZZA_TOPPINGS -> replyToPizzaToppings(chatId, message);
//            case AWAITING_CONFIRMATION -> replyToOrder(chatId, message);
//            default -> unexpectedMessage(chatId);
//        }
//    }

//    private void stopChat(long chatId) {
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(chatId);
//        sendMessage.setText("Thank you");
//        chatStats.remove(chatId);
//        sendMessage.setReplyMarkup(new ReplyKeyboardRemove( true));
//    }

    public SendMessage replyToWelcome(long chatId, Update update)
        throws SchedulerException
    {
        if ( userService.userIsExist( chatId ) ) {
            String link = update.getMessage().getText();
            userService.editUser(link,chatId);
            User userByChatId = userService.getUserByChatId( chatId );
            ScheduleTaskNewData.starScheduler( newAdBotService, userByChatId);
        }else {
            replyToStart( chatId );
            userService.adNewUser( chatId, "link",30);
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId( chatId );
        sendMessage.setText("Link został dodany");
        return sendMessage;
    }

    public SendMessage replyToStart(long chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId( chatId );
        sendMessage.setText(START_TEXT);
        return sendMessage;
    }

    public SendMessage promptWithKeyboardForState( long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(START_TEXT);
        sendMessage.setReplyMarkup(keyboardFactory.getAddOrDeleteLinks());
        return sendMessage;
    }

    private void changeUserStatus(UserStats userStats, User user){
        userService.changeUserStats( userStats, user );
    }

    @Override public void onApplicationEvent( AdEvent event )
    {
        Links links = event.getLinks();
        User user = links.getUser();
        Long userId = user.getId();
        Long chatId = user.getChatId();
        String latestUrlByUserId = userService.findLatestUrlByUserId( userId );
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId( chatId );
        sendMessage.setText(latestUrlByUserId );
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
