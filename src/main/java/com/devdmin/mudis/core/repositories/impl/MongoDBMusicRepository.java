package com.devdmin.mudis.core.repositories.impl;

import com.devdmin.mudis.core.model.Music;
import com.devdmin.mudis.core.repositories.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class MongoDBMusicRepository implements MusicRepository {

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public Music save(Music music) {
        mongoOperations.save(music);
        return music;
    }

    @Override
    public Music update(Music music, Music data) {
        if(data.getLink() != null) {
            music.setLink(data.getLink());
        }
        return music;
    }

    @Override
    public Music delete(String id) {
        Music music = find(id);
        mongoOperations.remove(music);
        return music;
    }

    @Override
    public Music find(String id) {
        return mongoOperations.findById(id, Music.class);
    }

    @Override
    public Music getByLink(String link) {
        return mongoOperations.findOne(query(where("link").is(link)), Music.class);
    }

    @Override
    public List<Music> getAllMusic() {
        List<Music> results = mongoOperations.findAll(Music.class);
        return results;
    }

    @Override
    public List<Music> getAllMusicByAccount(String id) {
        return mongoOperations.find(query(where("_id").in("account").is(id)), Music.class);
    }

    @Override
    public List<Music> getTopMusic(int amount) {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "votes"));
        System.out.println("========================");
        for (Music music : mongoOperations.find(query, Music.class)) {
            System.out.println(music.getVotes());
        }
        System.out.println("========================");
        return mongoOperations.find(query, Music.class);
    }

    @Override
    public List<Music> getLatestMusic(int amount) {
        Query query = new Query();
        query.limit(amount);
        query.with(new Sort(Sort.Direction.DESC, "date"));
        return mongoOperations.find(query, Music.class);
    }

}
