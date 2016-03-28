package com.devdmin.mudis.core.repositories;

import com.devdmin.mudis.core.config.WebAppConfiguration;
import com.devdmin.mudis.core.model.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  WebAppConfiguration.class)
@org.springframework.test.context.web.WebAppConfiguration
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository repository;

    private Account account;

    @Before
    @Transactional
    @Rollback(false)
    public void setup() {
        account = new Account();
        account.setName("name");
        account.setEmail("name@email.com");
        account.setPassword("password");
        repository.createAccount(account);
    }

    @Test
    @Transactional
    public void testFind()
    {
        Account account = repository.find(this.account.getId());
        assertNotNull(account);
        assertEquals(account.getName(), "name");
        assertEquals(account.getEmail(), "name@email.com");
        assertEquals(account.getPassword(), "password");
    }

    @Test
    @Transactional
    public void testFindByName()
    {
        Account account = repository.findByAccountName(this.account.getName());
        assertNotNull(account);
        assertEquals(account.getName(), "name");
        assertEquals(account.getEmail(), "name@email.com");
        assertEquals(account.getPassword(), "password");
    }

    @Test
    @Transactional
    public void testDelete()
    {
        repository.delete(this.account.getId());
        Account account = repository.find(this.account.getId());
        assertNull(account);
    }
}
