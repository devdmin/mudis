package com.devdmin.mudis.core.repositories;

import com.devdmin.mudis.core.model.Account;
import com.devdmin.mudis.core.model.Music;

import java.util.List;

public interface AccountRepository {
     Account find(String id);
     Account update(Account account, Account data);
     Account delete(String id);
     Account createAccount(Account account);
     Account findByAccountName(String username);
     List<Account> findAllAccounts();
}
