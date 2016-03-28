package com.devdmin.mudis.rest.mvc;


import com.devdmin.mudis.core.model.Account;
import com.devdmin.mudis.core.model.Music;
import com.devdmin.mudis.core.services.AccountService;
import com.devdmin.mudis.core.services.MusicService;
import com.devdmin.mudis.core.services.exceptions.AccountExistsException;
import com.devdmin.mudis.core.services.util.AccountList;
import com.devdmin.mudis.rest.exceptions.ConflictException;
import com.devdmin.mudis.rest.resources.AccountListResource;
import com.devdmin.mudis.rest.resources.AccountResource;

import com.devdmin.mudis.rest.resources.MusicResource;
import com.devdmin.mudis.rest.resources.asm.AccountListResourceAsm;

import com.devdmin.mudis.rest.resources.asm.AccountResourceAsm;
import com.devdmin.mudis.rest.resources.asm.MusicResourceAsm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;
import java.util.Date;
import java.util.Optional;


@Controller
@RequestMapping("/rest/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MusicService musicService;

    /*
     *
     *
     */
    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public ResponseEntity<AccountResource> getAccount(@PathVariable String name){
        return Optional.ofNullable(accountService.findByAccountName(name))
                .map(account -> {
                    AccountResource res = new AccountResourceAsm().toResource(account);
                    return new ResponseEntity<>(res, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<AccountResource> getAccountById(@PathVariable String id){
        return Optional.ofNullable(accountService.find(id))
                .map(account -> {
                    AccountResource res = new AccountResourceAsm().toResource(account);
                    return new ResponseEntity<>(res, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.PUT)
    public ResponseEntity<AccountResource> updateAccount(@PathVariable String name, @RequestBody AccountResource sentAccount) {
        return Optional.ofNullable(accountService.findByAccountName(name))
                .map(account -> {
                    Account updatedAccount = accountService.update(account, sentAccount.toAccount());
                    AccountResource res = new AccountResourceAsm().toResource(updatedAccount);
                    return new ResponseEntity<>(res, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.DELETE)
    public ResponseEntity<AccountResource> deleteAccount(@PathVariable String name){
        return Optional.ofNullable(accountService.findByAccountName(name))
                .map(account -> {
                    Account deletedAccount = accountService.delete(account.getId());
                    AccountResource res = new AccountResourceAsm().toResource(deletedAccount);
                    return new ResponseEntity<>(res, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/loggedUser", method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public ResponseEntity<AccountResource> getLoggedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails details = (UserDetails) principal;
            Account loggedIn = accountService.findByAccountName(details.getUsername());
            AccountResource res = new AccountResourceAsm().toResource(loggedIn);
            return new ResponseEntity<AccountResource>(res, HttpStatus.OK);
        }else {
            throw new ConflictException();
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public ResponseEntity<AccountListResource> getAllAccounts(){
        AccountList list = accountService.findAllAccounts();
        AccountListResource res = new AccountListResourceAsm().toResource(list);
        return new ResponseEntity<AccountListResource>(res, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("permitAll")
    public ResponseEntity<AccountResource> createAccount(@RequestBody AccountResource sentAccount){
        try {
            Account account = accountService.createAccount(sentAccount.toAccount());
            AccountResource res = new AccountResourceAsm().toResource(account);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(res.getLink("self").getHref()));
            return new ResponseEntity<AccountResource>(res, headers, HttpStatus.CREATED);
        }
        catch(AccountExistsException e){
            throw new ConflictException(e);
        }
    }

    @RequestMapping(value = "/{name}/music", method = RequestMethod.POST)
    @PreAuthorize("permitAll")
    public ResponseEntity<MusicResource> addMusic(@PathVariable String name, @RequestBody MusicResource sentMusic){
        sentMusic.setAuthor(accountService.findByAccountName(name));
        sentMusic.setDate(new Date());
        Music music = musicService.addMusic(sentMusic.toMusic());
        MusicResource musicResource = new MusicResourceAsm().toResource(music);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(musicResource.getLink("self").getHref()));
        return new ResponseEntity<MusicResource>(musicResource,httpHeaders, HttpStatus.CREATED);
    }
}
