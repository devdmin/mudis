package com.devdmin.mudis.core.services.impl;

import com.devdmin.mudis.core.model.Music;
import com.devdmin.mudis.core.repositories.MusicRepository;
import com.devdmin.mudis.core.services.MusicService;
import com.devdmin.mudis.core.services.exceptions.MusicDoesNotExist;
import com.devdmin.mudis.core.services.util.MusicList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class MusicServiceImpl implements MusicService {

    @Autowired
    private MusicRepository musicRepository;

    @Override
    @Transactional(readOnly = true)
    public Music find(String id) {
        return musicRepository.find(id);
    }

    @Override
    public Music update(Music music, Music data) {
        return musicRepository.update(music, data);
    }

    @Override
    public Music delete(String id) {
        return musicRepository.delete(id);
    }

    @Override
    public Music addMusic(Music music) {
        return musicRepository.save(music);
    }

    @Override
    public Music rateMusic(boolean rating, String musicId, String accountName) {
        Music music = find(musicId);
        Set<String> raters;
        int votes;
        if(music == null){
            throw new MusicDoesNotExist();
        }
        votes = music.getVotes();
        if(music.getRaters() != null) {
            raters = music.getRaters();
        }else{
            raters = new HashSet<String>();
        }
        raters.add(accountName);
        if(rating) {
            votes++;
        }else{
            votes--;
        }
        music.setVotes(votes);
        music.setRaters(raters);
        return musicRepository.save(music);
    }

    @Override
    @Transactional(readOnly = true)
    public Music getByLink(String link) {
        return musicRepository.getByLink(link);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isRater(String accountName, String musicId) {
        Music music = find(musicId);
        if(music.getRaters().contains(accountName))
            return true;
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public MusicList getAllMusic() {
        MusicList musicList = new MusicList(musicRepository.getAllMusic());
        return musicList;
    }

    @Override
    @Transactional(readOnly = true)
    public MusicList getTopMusic(int amount) {
        MusicList musicList = new MusicList(musicRepository.getTopMusic(amount));
        return musicList;
    }

    @Override
    @Transactional(readOnly = true)
    public MusicList getLatestMusic(int amount) {
        MusicList musicList = new MusicList(musicRepository.getLatestMusic(amount));
        return musicList;
    }

}
