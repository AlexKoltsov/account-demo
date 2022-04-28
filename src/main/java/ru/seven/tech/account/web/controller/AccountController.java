package ru.seven.tech.account.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.seven.tech.account.service.AccountService;
import ru.seven.tech.account.service.AccountsReader;
import ru.seven.tech.account.web.dto.AccountDto;
import ru.seven.tech.account.web.dto.NewAccountDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
@RestController
public class AccountController {

    // TODO: add validation on amount param. It cannot be negative

    private final AccountService accountService;
    private final AccountsReader accountsReader;

    @GetMapping
    public List<AccountDto> getAll() {
        return accountsReader.getAll();
    }

    @PostMapping
    public AccountDto openNewAccount(@RequestBody NewAccountDto newAccountDto) {
        return accountService.openNewAccount(newAccountDto);
    }

    @GetMapping("{id}")
    public AccountDto getById(@PathVariable UUID id) {
        return accountsReader.getById(id);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("{id}/deposit")
    public void depositMoney(@PathVariable UUID id, @RequestParam BigDecimal amount) {
        accountService.depositMoney(id, amount);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("{id}/withdraw")
    public void withdrawMoney(@PathVariable UUID id, @RequestParam BigDecimal amount) {
        accountService.withdrawMoney(id, amount);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("{id}/transfer")
    public void transferMoney(@PathVariable UUID id, @RequestParam UUID to, BigDecimal amount) {
        accountService.transferMoney(id, to, amount);
    }
}
