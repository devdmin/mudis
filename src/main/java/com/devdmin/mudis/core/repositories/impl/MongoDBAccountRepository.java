package com.devdmin.mudis.core.repositories.impl;

import com.devdmin.mudis.core.model.Account;
import com.devdmin.mudis.core.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class MongoDBAccountRepository implements AccountRepository {

    @Autowired
    private MongoOperations mongoOperation;

    @Override
    public Account find(String id) {
        Account account = mongoOperation.findById(id, Account.class);
        return account;
    }

    @Override
    public Account update(Account account, Account data) {
        if(data.getPassword() != null) {
            account.setPassword(data.getPassword());
        }
        if(data.getEmail() != null) {
            account.setEmail(data.getEmail());
        }
        mongoOperation.save(account);
        return account;
    }

    @Override
    public Account delete(String id) {
        Account account = find(id);
        mongoOperation.remove(find(id));
        return account;
    }

    @Override
    public Account createAccount(Account account) {
        mongoOperation.save(account);
        return account;
    }

    @Override
    public Account findByAccountName(String name) {
        return mongoOperation.findOne(query(where("name").is(name)), Account.class);
    }

    @Override
    public List<Account> findAllAccounts() {
        List<Account> accountList = mongoOperation.findAll(Account.class);
        return accountList;
    }

}
