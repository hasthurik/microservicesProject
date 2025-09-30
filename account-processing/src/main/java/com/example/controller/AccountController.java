package com.example.controller;

import com.example.entity.Account;
import com.example.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        accountService.saveAccount(account);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<Account>> getAccounts() {
        return new ResponseEntity<>(accountService.findAllAccounts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        return new ResponseEntity<>(accountService.findById(id), HttpStatus.OK);
    }

}
