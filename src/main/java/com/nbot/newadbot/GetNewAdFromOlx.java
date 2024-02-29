package com.nbot.newadbot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetNewAdFromOlx {
    public static List<String> getElement(String url){
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
}
