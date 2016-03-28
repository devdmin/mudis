package com.devdmin.mudis.rest.resources;

import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.List;

public class MusicListResource extends ResourceSupport {
    private List<MusicResource> music = new ArrayList<MusicResource>();
    public List<MusicResource> getMusic(){
        return music;
    }

    public void setMusic(List<MusicResource> music){
        this.music = music;
    }
}
