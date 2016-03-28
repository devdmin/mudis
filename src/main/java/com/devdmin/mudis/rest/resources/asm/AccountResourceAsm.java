package com.devdmin.mudis.rest.resources.asm;

import com.devdmin.mudis.core.model.Account;
import com.devdmin.mudis.rest.mvc.AccountController;
import com.devdmin.mudis.rest.resources.AccountResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class AccountResourceAsm extends ResourceAssemblerSupport<Account, AccountResource> {

    public AccountResourceAsm() {
        super(AccountController.class, AccountResource.class);
    }

    @Override
    public AccountResource toResource(Account account) {
        AccountResource res = new AccountResource();
        res.setRid(account.getId());
        res.setName(account.getName());
        res.setEmail(account.getEmail());
        res.setPassword(account.getPassword());
        Link link = linkTo(methodOn(AccountController.class).getAccount(account.getName())).withRel("self");
        res.add(link);
        return res;
    }
}
