package ru.seven.tech.account.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.seven.tech.account.service.AccountService;
import ru.seven.tech.account.service.AccountsReader;
import ru.seven.tech.account.web.dto.AccountDto;
import ru.seven.tech.account.web.dto.NewAccountDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
@RestController
public class AccountController {

    private final AccountService accountService;
    private final AccountsReader accountsReader;

    @GetMapping
    public List<AccountDto> getAll() {
        return accountsReader.getAll();
    }

    @PostMapping
    public AccountDto openNewAccount(@RequestBody @Valid NewAccountDto newAccountDto) {
        return accountService.openNewAccount(newAccountDto);
    }

    @GetMapping("{id}")
    public AccountDto getById(@PathVariable UUID id) {
        return accountsReader.getById(id);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("{id}/deposit")
    public void depositMoney(@PathVariable UUID id, @RequestParam @Positive BigDecimal amount) {
        accountService.depositMoney(id, amount);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("{id}/withdraw")
    public void withdrawMoney(@PathVariable UUID id, @RequestParam @Positive BigDecimal amount) {
        accountService.withdrawMoney(id, amount);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("{id}/transfer")
    public void transferMoney(@PathVariable UUID id, @RequestParam UUID to, @Positive BigDecimal amount) {
        accountService.transferMoney(id, to, amount);
    }
}
