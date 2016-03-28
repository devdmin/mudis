package com.devdmin.mudis.rest.resources.asm;

import com.devdmin.mudis.core.services.util.AccountList;
import com.devdmin.mudis.rest.mvc.AccountController;
import com.devdmin.mudis.rest.resources.AccountListResource;
import com.devdmin.mudis.rest.resources.AccountResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.List;

public class AccountListResourceAsm extends ResourceAssemblerSupport<AccountList, AccountListResource> {

    public AccountListResourceAsm() {
        super(AccountController.class, AccountListResource.class);
    }

    @Override
    public AccountListResource toResource(AccountList accountList) {
        List<AccountResource> resList = new AccountResourceAsm().toResources(accountList.getAccountList());
        AccountListResource finalRes = new AccountListResource();
        finalRes.setAccounts(resList);
        return finalRes;
    }


}
