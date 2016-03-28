package com.devdmin.mudis.rest.mvc;

import com.devdmin.mudis.core.model.Account;
import com.devdmin.mudis.core.services.AccountService;
import com.devdmin.mudis.core.services.exceptions.AccountExistsException;
import com.devdmin.mudis.core.services.util.AccountList;
import com.devdmin.mudis.rest.mvc.AccountController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest {
    @InjectMocks
    private AccountController controller;

    @Mock
    private AccountService service;

    private MockMvc mockMvc;
    private ArgumentCaptor<Account> accountCaptor;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        accountCaptor = ArgumentCaptor.forClass(Account.class);
    }


    @Test
    public void createAccountNonExistingUsername() throws Exception {
        Account createdAccount = new Account();

        createdAccount.setName("test");
        createdAccount.setEmail("test@email.com");
        createdAccount.setPassword("test");

        when(service.createAccount(any(Account.class))).thenReturn(createdAccount);

        mockMvc.perform(post("/rest/accounts")
                .content("{\"name\":\"test\",\"password\":\"test\",\"email\":\"test@email.com\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(createdAccount.getName())))
                .andExpect(jsonPath("$.email", is(createdAccount.getEmail())))
                .andExpect(status().isCreated());

        verify(service).createAccount(accountCaptor.capture());
        String password = accountCaptor.getValue().getPassword();
        assertEquals("test", password);
    }

    @Test
    public void createAccountExistingUsername() throws Exception{
        Account createdAccount = new Account();
        createdAccount.setPassword("test");
        createdAccount.setName("test");
        createdAccount.setEmail("test@test.com");

        when(service.createAccount(any(Account.class))).thenThrow(new AccountExistsException());

        mockMvc.perform(post("/rest/accounts")
                .content("{\"name\":\"test\",\"password\":\"test\",\"email\":\"test@test.com\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void getExistingAccount() throws Exception{
        Account account = new Account();

        account.setName("test");
        account.setPassword("password");
        account.setEmail("email@email.com");

        when(service.findByAccountName("test")).thenReturn(account);

        mockMvc.perform(get("/rest/accounts/test"))
                .andExpect(jsonPath("$.password", is(nullValue())))
                .andExpect(jsonPath("$.name", is(account.getName())))
                .andExpect(status().isOk());
    }

    @Test
    public void getNonExistingAccount() throws Exception {
        when(service.findByAccountName("test")).thenReturn(null);

        mockMvc.perform(get("/rest/accounts/test"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllAccounts() throws Exception {
        List<Account> accounts = new ArrayList<Account>();

        Account accountA = new Account();

        accountA.setName("testA");
        accountA.setPassword("passwordA");
        accountA.setEmail("emailA@email.com");


        Account accountB = new Account();

        accountB.setName("testB");
        accountB.setPassword("passwordB");
        accountB.setEmail("emailB@email.com");

        accounts.add(accountA);
        accounts.add(accountB);

        AccountList accountList = new AccountList(accounts);

        when(service.findAllAccounts()).thenReturn(accountList);

        mockMvc.perform(get("/rest/accounts"))
                .andExpect(jsonPath("$.accounts[*].name",
                        hasItems(endsWith("testA"), endsWith("testB"))))
                .andExpect(status().isOk());
    }

    @Test
    public void updateExistingAccount() throws Exception{
        Account accountA = new Account();

        accountA.setName("testA");
        accountA.setPassword("passwordA");
        accountA.setEmail("emailA@email.com");

        Account accountB = new Account();

        accountB.setName("testA");
        accountB.setPassword("passwordB");
        accountB.setEmail("emailB@email.com");


        when(service.findByAccountName("testA")).thenReturn(accountA);
        when(service.update(any(Account.class), any(Account.class))).thenReturn(accountB);

        mockMvc.perform(put("/rest/accounts/testA")
                .content("{\"name\":\"testA\",\"password\":\"passwordB\",\"email\":\"emailB@email.com\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(accountA.getName())))
                .andExpect(jsonPath("$.email", is(accountB.getEmail())))
                .andExpect(status().isOk());

        verify(service).update(any(Account.class), accountCaptor.capture());
        String password = accountCaptor.getValue().getPassword();
        assertEquals("passwordB", password);
    }

    @Test
    public void updateNonExistingAccount() throws Exception{
        when(service.findByAccountName("testA")).thenReturn(null);

        mockMvc.perform(put("/rest/accounts/testA")
                .content("{\"name\":\"testA\",\"password\":\"passwordB\",\"email\":\"emailB@email.com\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteExistingAccount() throws Exception{
        Account account = new Account();

        account.setName("test");
        account.setPassword("password");
        account.setEmail("email@email.com");

        when(service.findByAccountName("test")).thenReturn(account);
        when(service.delete(any(String.class))).thenReturn(account);

        mockMvc.perform(delete("/rest/accounts/test"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteNonExistingAccount() throws Exception{
        when(service.findByAccountName("test")).thenReturn(null);
        mockMvc.perform(delete("/rest/accounts/test"))
                .andExpect(status().isNotFound());
    }
}
