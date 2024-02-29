package com.nbot.newadbot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource( "application.yml" )
public class BotConfig
{
    @Value( "${bot.name}" )
    String botName;
    @Value( "${bot.token}" )
    String botToken;
}
