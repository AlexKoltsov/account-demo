package ru.seven.tech.account.service;

import ru.seven.tech.account.web.dto.AccountDto;
import ru.seven.tech.account.web.dto.NewAccountDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface AccountService {
    AccountDto openNewAccount(NewAccountDto newAccountDto);

    void depositMoney(UUID id, BigDecimal amount);

    void withdrawMoney(UUID id, BigDecimal amount);

    void transferMoney(UUID from, UUID to, BigDecimal amount);

}
