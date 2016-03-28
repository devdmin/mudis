package com.devdmin.mudis.rest.resources;


import com.devdmin.mudis.core.model.Account;
import com.devdmin.mudis.core.model.Music;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

public class MusicResource extends ResourceSupport{
    private String rid;
    private String link;
    private Account author;
    private int votes;
    private Date date;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Account getAuthor() {
        return author;
    }

    public void setAuthor(Account author) {
        this.author = author;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public Music toMusic(){
        Music music = new Music();
        music.setAuthor(author);
        music.setLink(link);
        music.setDate(date);
        music.setVotes(votes);
        music.setId(rid);
        return music;
    }

}
