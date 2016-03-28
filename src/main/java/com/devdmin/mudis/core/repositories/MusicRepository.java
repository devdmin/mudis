package com.devdmin.mudis.core.repositories;

import com.devdmin.mudis.core.model.Music;

import java.util.List;

public interface MusicRepository {
     Music save(Music music);
     Music update(Music music, Music data);
     Music delete(String id);
     Music find(String id);
     Music getByLink(String link);
     List<Music> getAllMusic();
     List<Music> getAllMusicByAccount(String id);
     List<Music> getTopMusic(int amount);
     List<Music> getLatestMusic(int amount);

}
