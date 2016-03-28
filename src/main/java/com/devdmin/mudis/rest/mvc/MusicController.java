package com.devdmin.mudis.rest.mvc;

import com.devdmin.mudis.core.model.Account;
import com.devdmin.mudis.core.model.Music;
import com.devdmin.mudis.core.services.MusicService;
import com.devdmin.mudis.core.services.exceptions.MusicDoesNotExist;
import com.devdmin.mudis.core.services.util.BooleanJson;
import com.devdmin.mudis.core.services.util.MusicList;
import com.devdmin.mudis.rest.exceptions.ConflictException;
import com.devdmin.mudis.rest.resources.AccountResource;
import com.devdmin.mudis.rest.resources.MusicListResource;
import com.devdmin.mudis.rest.resources.MusicResource;
import com.devdmin.mudis.rest.resources.asm.AccountResourceAsm;
import com.devdmin.mudis.rest.resources.asm.MusicListResourceAsm;
import com.devdmin.mudis.rest.resources.asm.MusicResourceAsm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/rest/music")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @RequestMapping(value = "/{musicId}", method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public ResponseEntity<MusicResource> getMusic(@PathVariable String musicId){
        return Optional.ofNullable(musicService.find(musicId))
                .map(music -> {
                    MusicResource res = new MusicResourceAsm().toResource(music);
                    return new ResponseEntity<>(res, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{musicId}", method = RequestMethod.DELETE)
    public ResponseEntity<MusicResource> deleteMusic(@PathVariable String musicId){
        return Optional.ofNullable(musicService.find(musicId))
                .map(music -> {
                    Music deletedMusic = musicService.delete(music.getId());
                    MusicResource res = new MusicResourceAsm().toResource(deletedMusic);
                    return new ResponseEntity<>(res, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{musicId}", method = RequestMethod.PUT)
    public ResponseEntity<MusicResource> updateMusic(@PathVariable String musicId, @RequestBody MusicResource sentMusic) {
        return Optional.ofNullable(musicService.find(musicId))
                .map(music -> {
                    Music updatedMusic = musicService.update(music, sentMusic.toMusic());
                    MusicResource res = new MusicResourceAsm().toResource(updatedMusic);
                    return new ResponseEntity<>(res, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{musicId}/rate", method = RequestMethod.POST)
    @PreAuthorize("permitAll")
    public ResponseEntity<MusicResource> rateMusic(@PathVariable String musicId, @RequestParam("thumbUp") boolean sentRating){
        try{
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                    UserDetails details = (UserDetails) principal;
                Music musicToCheck = musicService.find(musicId);
                if( musicToCheck.getRaters() == null || !musicToCheck.getRaters().contains(details.getUsername())) {
                    Music music = musicService.rateMusic(sentRating, musicId, details.getUsername());
                    MusicResource res = new MusicResourceAsm().toResource(music);
                    return new ResponseEntity<>(res, HttpStatus.OK);
                } else{
                    throw new ConflictException();
                }
            }else {
                throw new ConflictException();
            }
        }
        catch(MusicDoesNotExist e){
            throw new ConflictException(e);
        }
    }

    @RequestMapping(value = "/latest/{amount}", method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public ResponseEntity<MusicListResource> getLatestMusic(@PathVariable int amount){
        MusicList musicList = musicService.getLatestMusic(amount);
        MusicListResource res = new MusicListResourceAsm().toResource(musicList);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @RequestMapping(value = "/top/{amount}", method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public ResponseEntity<MusicListResource> getTopMusic(@PathVariable int amount){
        MusicList musicList = musicService.getTopMusic(amount);
        MusicListResource res = new MusicListResourceAsm().toResource(musicList);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @RequestMapping(value = "/{musicId}/isRated", method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public ResponseEntity<BooleanJson> isRated(@PathVariable String musicId){
        Music music = musicService.find(musicId);
        BooleanJson isRated;
        if(getAccountName() != null && music != null && music.getRaters() != null) {
            isRated = new BooleanJson(false);
            if (music.getRaters().contains(getAccountName()))
                isRated = new BooleanJson(true);
                return new ResponseEntity<>(isRated, HttpStatus.OK);
        }
        isRated = new BooleanJson(false);
        return new ResponseEntity<>(isRated, HttpStatus.OK);
    }

    private String getAccountName(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails details = (UserDetails) principal;
            return details.getUsername();
        }
        return null;
    }
}
