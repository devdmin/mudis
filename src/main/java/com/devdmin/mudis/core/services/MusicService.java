package com.devdmin.mudis.core.services;

import com.devdmin.mudis.core.model.Music;
import com.devdmin.mudis.core.services.util.MusicList;

import java.util.List;

public interface MusicService {
     Music find(String id);
     Music update(Music music, Music data);
     Music delete(String id);
     Music addMusic(Music music);
     Music rateMusic(boolean rating, String musicId, String accountName);
     Music getByLink(String link);
     boolean isRater(String accountName, String musicId);
     MusicList getAllMusic();
     MusicList getTopMusic(int amount);
     MusicList getLatestMusic(int amount);
}
