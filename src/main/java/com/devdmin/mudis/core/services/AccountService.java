package com.devdmin.mudis.core.services;


import com.devdmin.mudis.core.model.Account;
import com.devdmin.mudis.core.model.Music;
import com.devdmin.mudis.core.services.util.AccountList;
import com.devdmin.mudis.core.services.util.MusicList;

import java.util.Optional;

public interface AccountService {

     Account find(String id);
     Account update(Account account, Account data);
     Account delete(String id);
     Account createAccount(Account account);
     Account findByAccountName(String username);
     AccountList findAllAccounts();
     Music addMusic(String id, Music music);
     MusicList getAllMusicByAccount(String id);

}
