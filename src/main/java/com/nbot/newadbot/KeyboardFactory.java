package com.nbot.newadbot;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
@Service
public class KeyboardFactory
{
    public static ReplyKeyboard getAddOrDeleteLinks(){
        KeyboardRow row = new KeyboardRow();
        row.add("Dodaj nowy link");
        row.add("Usun link");
        return new ReplyKeyboardMarkup( List.of( row));
    }

    public static ReplyKeyboard getTimeToRefresh(){
        KeyboardRow row = new KeyboardRow();
        row.add("Ustaw czas odświeżania");
        row.add("Zmień czas odświeżania");
        return new ReplyKeyboardMarkup( List.of( row));
    }
}
