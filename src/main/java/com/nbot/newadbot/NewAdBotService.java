package com.nbot.newadbot;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class NewAdBotService {
    private final UserRepository userRepository;
    private final LinksRepository linksRepository;

    public NewAdBotService(UserRepository userRepository, LinksRepository linksRepository) {
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
    public void checkForNewData(String chatId){
        User user = userRepository.getUserByChatId(chatId).orElseThrow();
        String userUrl = user.getLink();

        Links links = linksRepository.findByUser(user)
                .orElseGet(() -> new Links(user, new HashSet<>()));
//        LocalDateTime now = LocalDateTime.now();
//        String format = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
//        log.debug(format + " Checking for new data... " + user.getId());

        try{
            List<String> newData = getElement(userUrl);
            Set<String> urlSet = links.getUrlSet();
            for (String element : newData) {
                if (!urlSet.contains(element)) {
//                    System.out.println(element);
                    urlSet.add(element);
                }
            }
            links.setUrlSet(urlSet);
            linksRepository.save(links);
//            log.debug("Current links: " + links.getUrlSet().size());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
