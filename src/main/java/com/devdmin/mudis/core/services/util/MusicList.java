package com.devdmin.mudis.core.services.util;

import com.devdmin.mudis.core.model.Music;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MusicList{
    private List<Music> musicList = new ArrayList<Music>();

    public MusicList(List<Music> musicList) {
        this.musicList = musicList;
    }

    public List<Music> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
    }

}
