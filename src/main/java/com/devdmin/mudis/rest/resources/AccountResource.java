package com.devdmin.mudis.rest.resources;

import com.devdmin.mudis.core.model.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.util.Set;

public class AccountResource extends ResourceSupport {
    private String rid;
    private String name;
    private String password;
    private String email;
    

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Account toAccount(){
        Account account = new Account();
        account.setId(rid);
        account.setName(name);
        account.setEmail(email);
        account.setPassword(password);
        return account;
    }
}
