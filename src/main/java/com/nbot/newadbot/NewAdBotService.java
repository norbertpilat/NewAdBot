package com.nbot.newadbot;

import com.nbot.newadbot.links.Links;
import com.nbot.newadbot.links.LinksRepository;
import com.nbot.newadbot.user.User;
import com.nbot.newadbot.user.UserRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class NewAdBotService {
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final LinksRepository linksRepository;

    public NewAdBotService( ApplicationEventPublisher eventPublisher, UserRepository userRepository, LinksRepository linksRepository) {
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
        this.linksRepository = linksRepository;
    }
    public List<String> getElement(String url){
        List<String> newData = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();

            Elements links = document.select("a.css-rc5s2u");

            for (Element link : links) {
                String href1 = "https://www.olx.pl/" + link.attr("href");
                newData.add(href1);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return newData;
    }
    @Transactional
    public void checkForNewData(long chatId){
        User user = userRepository.getUserByChatId( chatId).orElseThrow();
        String userUrl = user.getLink();

        Links links = linksRepository.findByUser( user)
                .orElseGet(() -> new Links("Nowa lista",user ));
//        log.debug(format + " Checking for new data... " + user.getId());

        try{
            List<String> newData = getElement(userUrl);
            List<Links> allUrls = linksRepository.findAllUrlsByUserId( user.getId() );
            for (String element : newData) {
                Links newLinks = new Links( element, user );
                allUrls.add(newLinks);
              eventPublisher.publishEvent( new AdEvent( links ) );
            }
            linksRepository.save( links );
                linksRepository.saveAll(allUrls);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
