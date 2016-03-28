package com.devdmin.mudis.core.services.impl;

import com.devdmin.mudis.core.model.Account;
import com.devdmin.mudis.core.model.Music;
import com.devdmin.mudis.core.repositories.AccountRepository;
import com.devdmin.mudis.core.repositories.MusicRepository;
import com.devdmin.mudis.core.services.AccountService;
import com.devdmin.mudis.core.services.exceptions.AccountDoesNotExistsException;
import com.devdmin.mudis.core.services.exceptions.AccountExistsException;
import com.devdmin.mudis.core.services.exceptions.MusicExistiException;
import com.devdmin.mudis.core.services.util.AccountList;
import com.devdmin.mudis.core.services.util.MusicList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MusicRepository musicRepository;

    @Override
    @Transactional(readOnly = true)
    public Account find(String id) {
        return accountRepository.find(id);
    }

    @Override
    public Account update(Account account, Account data) {
        return accountRepository.update(account, data);
    }

    @Override
    public Account delete(String id) {
        return accountRepository.delete(id);
    }

    @Override
    public Account createAccount(Account account) {
        Account accountToCheck = accountRepository.findByAccountName(account.getName());
        if(accountToCheck != null){
            throw new AccountExistsException();
        }
        return accountRepository.createAccount(account);
    }

    @Override
    @Transactional(readOnly = true)
    public Account findByAccountName(String username) {
        return accountRepository.findByAccountName(username);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountList findAllAccounts() {
        AccountList accountList = new AccountList(accountRepository.findAllAccounts());
        return accountList;
    }

    @Override
    public Music addMusic(String id, Music music) {
        Account account = accountRepository.find(id);
        Music musicToCheck = musicRepository.getByLink(music.getLink());
        if(account == null){
            throw new AccountDoesNotExistsException();
        }
        if(musicToCheck != null){
            throw new MusicExistiException();
        }
        music.setAuthor(account);
        return musicRepository.save(music);
    }

    @Override
    @Transactional(readOnly = true)
    public MusicList getAllMusicByAccount(String id) {
        Account account = accountRepository.find(id);
        if(account == null){
            throw new AccountDoesNotExistsException();
        }
        MusicList musicList = new MusicList(musicRepository.getAllMusicByAccount(id));
        return musicList;
    }
}
