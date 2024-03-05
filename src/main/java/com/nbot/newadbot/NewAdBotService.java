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

import java.util.*;

@Service
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class NewAdBotService {
    private final UserRepository userRepository;
    private final LinksRepository linksRepository;
    private final ApplicationEventPublisher eventPublisher;
    @Transactional
    public void checkForNewData(long chatId){
        Optional<User> optionalUser = userRepository.getUserByChatId(chatId);
        User user = optionalUser.orElseThrow(() -> new RuntimeException("User not found"));
        String userUrl = user.getLink();

        boolean existingLinks = linksRepository.existsByUser(user);

        if (existingLinks) {
            try {
                List<String> newData = GetNewAdFromOlx.getElement(userUrl);
                Set<Links> allUrls = linksRepository.findAllUrlsByUserId(user.getId())
                        .orElseGet(HashSet::new);
                for (String element : newData) {
                    boolean urlExists = allUrls.stream()
                            .anyMatch(allUrl -> allUrl.getUrl() != null && allUrl.getUrl().equals(element));

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
