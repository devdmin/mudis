package com.devdmin.mudis.rest.resources.asm;


import com.devdmin.mudis.core.services.util.MusicList;
import com.devdmin.mudis.rest.mvc.MusicController;
import com.devdmin.mudis.rest.resources.MusicListResource;
import com.devdmin.mudis.rest.resources.MusicResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.List;

public class MusicListResourceAsm extends ResourceAssemblerSupport<MusicList, MusicListResource> {
    public MusicListResourceAsm(){
        super(MusicController.class, MusicListResource.class);
    }

    @Override
    public MusicListResource toResource(MusicList musicList){
        List<MusicResource> resList = new MusicResourceAsm().toResources(musicList.getMusicList());
        MusicListResource finalRes = new MusicListResource();
        finalRes.setMusic(resList);
        return finalRes;
    }

}
