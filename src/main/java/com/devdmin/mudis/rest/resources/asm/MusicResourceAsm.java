package com.devdmin.mudis.rest.resources.asm;

import com.devdmin.mudis.core.model.Music;
import com.devdmin.mudis.rest.mvc.AccountController;
import com.devdmin.mudis.rest.mvc.MusicController;
import com.devdmin.mudis.rest.resources.MusicResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class MusicResourceAsm extends ResourceAssemblerSupport<Music, MusicResource> {
    public MusicResourceAsm() {
        super(MusicController.class, MusicResource.class);
    }

    @Override
    public MusicResource toResource(Music music) {
        MusicResource res = new MusicResource();
        res.setRid(music.getId());
        res.setLink(music.getLink());
        res.setAuthor(music.getAuthor());
        res.setDate(music.getDate());
        res.setVotes(music.getVotes());
        Link link = linkTo(MusicController.class).slash(music.getId()).withSelfRel();
        res.add(link);
        if(music.getAuthor() != null) {
            Link authorLink = linkTo(methodOn(AccountController.class).getAccount(music.getAuthor().getName())).withRel("author");
            res.add(authorLink);
        }
        return res;
    }


}
