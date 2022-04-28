package ru.seven.tech.account.service;

import ru.seven.tech.account.web.dto.AccountDto;

import java.util.List;
import java.util.UUID;

public interface AccountsReader {
    List<AccountDto> getAll();

    AccountDto getById(UUID id);
}
