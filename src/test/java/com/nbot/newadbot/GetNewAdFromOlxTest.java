package com.nbot.newadbot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThatList;
import static org.junit.jupiter.api.Assertions.*;
class GetNewAdFromOlxTest {
    @Test
    void shouldReturnNotEmptyList(){
        //given
        String url = "https://www.olx.pl/elektronika/smartwatche-i-opaski/q-apple-watch/?search%5Border%5D=created_at:desc&search%5Bfilter_enum_state%5D%5B0%5D=damaged";

        //when
        List<String> element = GetNewAdFromOlx.getElement(url);

        //then
        assertThatList(element).isNotEmpty();
    }

    @Test
    void shouldReturn41ElementsInList(){
        //given
        String url = "https://www.olx.pl/elektronika/smartwatche-i-opaski/q-apple-watch/?search%5Border%5D=created_at:desc&search%5Bfilter_enum_state%5D%5B0%5D=damaged";

        //when
        List<String> element = GetNewAdFromOlx.getElement(url);

        //then
        assertEquals(41, element.size());
    }
}