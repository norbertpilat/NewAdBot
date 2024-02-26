package com.nbot.newadbot.user;

import com.nbot.newadbot.UserStats;
import com.nbot.newadbot.links.Links;
import com.nbot.newadbot.links.LinksRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService
{
    private final UserRepository userRepository;
    private final LinksRepository linksRepository;


    public User getUserByChatId(long chatId){
       return userRepository.getUserByChatId( chatId ).orElseThrow();
    }

    public String findLatestUrlByUserId(Long userId){
        return linksRepository.findLatestUrlByUserId(userId);
    }

    public boolean userIsExist(long chatId){
       return userRepository.getUserByChatId( chatId ).isPresent();
    }

    @Transactional
    public void changeUserStats( UserStats userStats, User user){
        User userFromDb = getUserByChatId( user.getChatId() );
        userFromDb.setChatId( user.getChatId() );
        userFromDb.setLink( user.getLink() );
        userFromDb.setOldData( user.getOldData() );
        userFromDb.setTimeRefresh( user.getTimeRefresh() );
        userFromDb.setChatStatus( userStats );
        userRepository.save( userFromDb );
    }

    @Transactional
    public void adNewUser(Long chatId,String link,int timeRefresh){
        User user = new User();
        user.setChatId( chatId );
        user.setLink( link );
        user.setTimeRefresh( timeRefresh );
        user.setChatStatus( UserStats.MENU );
        userRepository.save( user );
    }

    @Transactional
    public void editUser(String link, long chatId){
        User userByChatId = getUserByChatId( chatId );
        userByChatId.setChatId( userByChatId.getChatId() );
        userByChatId.setLink( link );
        userByChatId.setTimeRefresh( userByChatId.getTimeRefresh() );
        userByChatId.setChatStatus( userByChatId.getChatStatus() );
        userRepository.save( userByChatId );
    }
}
