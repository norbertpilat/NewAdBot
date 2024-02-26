package com.nbot.newadbot;

import com.nbot.newadbot.links.Links;
import com.nbot.newadbot.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
@Getter
@Setter
public class AdEvent extends ApplicationEvent
{
    private Links links;
    public AdEvent(Links links )
    {
        super( links );
        this.links = links;
    }
}
