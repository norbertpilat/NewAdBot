package com.nbot.newadbot;


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

        boolean existingLinks = linksRepository.existsByUser(user);

        if (existingLinks) {
            // ... reszta kodu
            try {
                List<String> newData = getElement(userUrl);
                List<Links> allUrls = linksRepository.findAllUrlsByUserId(user.getId());
                int startSize = allUrls.size();

                if (allUrls == null) {
                    allUrls = new ArrayList<>();
                }

                for (String element : newData) {
                    boolean urlExists = false;

                    for (Links allUrl : allUrls) {
                        if (allUrl.getUrl() != null && allUrl.getUrl().equals(element)) {
                            urlExists = true;
                            break;
                        }
                    }

                    if (!urlExists) {
                        Links newLinks = new Links(user, element);
                        allUrls.add(newLinks);
                        System.out.println(newLinks.getUrl());
                        // eventPublisher.publishEvent(new AdEvent(links));
                    }
                }
                int endSize = allUrls.size();
                int newLinks = endSize - startSize;
                System.out.println(newLinks);
                linksRepository.saveAll(allUrls);



            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            Links createLinks = new Links(user, "Nowa lista");
            // ... reszta kodu
            linksRepository.save(createLinks);
        }
    }

}
