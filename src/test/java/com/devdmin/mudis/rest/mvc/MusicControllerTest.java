package com.devdmin.mudis.rest.mvc;

import com.devdmin.mudis.core.model.Account;
import com.devdmin.mudis.core.model.Music;
import com.devdmin.mudis.core.services.MusicService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.endsWith;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MusicControllerTest {
    @InjectMocks
    private MusicController controller;

    @Mock
    private MusicService service;

    private MockMvc mockMvc;


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getExistingMusic() throws Exception{
        Music music = new Music();
        music.setId("2ed33d32f2");
        music.setLink("www.reddit.com");

        when(service.find("2ed33d32f2")).thenReturn(music);

        mockMvc.perform(get("/rest/music/2ed33d32f2"))
                .andExpect(jsonPath("$.link", is(music.getLink())))
                .andExpect(status().isOk());
    }

    @Test
    public void getNonExistingMusic() throws Exception{
        when(service.find("2ed33d32f2")).thenReturn(null);
        mockMvc.perform(get("/rest/music/2ed33d32f2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateExistingMusic() throws Exception{
        Account accountA = new Account();
        Date date = new Date();
        accountA.setName("testA");
        accountA.setPassword("passwordA");
        accountA.setEmail("emailA@email.com");

        Music musicA = new Music();
        musicA.setId("dshd7hs7ahd");
        musicA.setAuthor(accountA);
        musicA.setLink("www.test.com");
        musicA.setDate(date);

        Music musicB = new Music();
        musicB.setId("dshd7hs7ahd");
        musicB.setAuthor(accountA);
        musicB.setLink("www.test10.com");
        musicB.setDate(date);

        when(service.find(musicA.getId())).thenReturn(musicA);
        when(service.update(any(Music.class), any(Music.class))).thenReturn(musicB);

        mockMvc.perform(put("/rest/music/dshd7hs7ahd")
                .content("{\"link\":\"www.test10.com\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.link", is(musicB.getLink())))
                .andExpect(jsonPath("$.rid", is(musicB.getId())))
                .andExpect(status().isOk());
    }

    @Test
    public void updateNonExistingMusic() throws Exception{
        when(service.find("dshd7hs7ahd")).thenReturn(null);

        mockMvc.perform(put("/rest/music/dshd7hs7ahd")
                .content("{\"link\":\"www.test10.com\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
