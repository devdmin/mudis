package com.devdmin.mudis.core.services.util;

import com.devdmin.mudis.core.model.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountList {
    private List<Account> accountList = new ArrayList<Account>();

    public AccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }
}
