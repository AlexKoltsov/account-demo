package ru.seven.tech.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.seven.tech.account.utils.AccountUtils;
import ru.seven.tech.account.component.AccountMapper;
import ru.seven.tech.account.data.entity.Account;
import ru.seven.tech.account.data.repo.AccountRepository;
import ru.seven.tech.account.expcetions.EntityNotFoundException;
import ru.seven.tech.account.web.dto.AccountDto;
import ru.seven.tech.account.web.dto.NewAccountDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AccountsServiceImpl implements AccountService, AccountsReader {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public AccountDto openNewAccount(NewAccountDto newAccountDto) {
        Account account = accountMapper.toEntity(newAccountDto);
        accountRepository.save(account);
        return accountMapper.toDto(account);
    }

    @Override
    public void depositMoney(UUID id, BigDecimal amount) {
        Account account = getAccountById(id);
        AccountUtils.increaseAccountsBalance(account, amount);
        accountRepository.save(account);
    }

    @Override
    public void withdrawMoney(UUID id, BigDecimal amount) {
        // TODO: add validation on negative balance
        Account account = getAccountById(id);
        AccountUtils.decreaseAccountsBalance(account, amount);
        accountRepository.save(account);
    }

    @Transactional
    @Override
    public void transferMoney(UUID from, UUID to, BigDecimal amount) {
        withdrawMoney(from, amount);
        depositMoney(to, amount);
    }

    @Override
    public List<AccountDto> getAll() {
        return accountRepository.findAll().stream()
                .map(accountMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDto getById(UUID id) {
        Account account = getAccountById(id);
        return accountMapper.toDto(account);
    }

    private Account getAccountById(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Account with id [%s] does not exist", id)));
    }
}
