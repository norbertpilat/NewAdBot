package com.nbot.newadbot;

import com.nbot.newadbot.links.Links;
import com.nbot.newadbot.links.LinksRepository;
import com.nbot.newadbot.user.User;
import com.nbot.newadbot.user.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class NewAdBotService {
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final LinksRepository linksRepository;

    @Transactional
    public void checkForNewData(Long chatId){
        User user = userRepository.getUserByChatId(chatId).orElseThrow();
        String userUrl = user.getLink();

        boolean existingLinks = linksRepository.existsByUser(user);

        if (existingLinks) {
            try {
                List<String> newData = GetNewAdFromOlx.getElement(userUrl);
                List<Links> allUrls = linksRepository.findAllUrlsByUserId(user.getId());

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
                        eventPublisher.publishEvent(new AdEvent(newLinks));
                    }
                }
                linksRepository.saveAll(allUrls);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            Links createLinks = new Links(user, "Nowa lista");
            linksRepository.save(createLinks);
        }
    }
}
