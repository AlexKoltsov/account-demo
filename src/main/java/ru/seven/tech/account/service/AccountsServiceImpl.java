package ru.seven.tech.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
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

    @Transactional
    @Override
    public void depositMoney(UUID id, BigDecimal amount) {
        log.debug("Deposit {} money for {} account", amount, id);
        Account account = getAccountById(accountRepository::findByIdForUpdate, id);
        AccountUtils.increaseAccountsBalance(account, amount);
        accountRepository.save(account);
    }

    @Transactional
    @Override
    public void withdrawMoney(UUID id, BigDecimal amount) {
        log.debug("Withdraw {} money for {} account", amount, id);
        Account account = getAccountById(accountRepository::findByIdForUpdate, id);
        AccountUtils.decreaseAccountsBalance(account, amount);
        accountRepository.save(account);
    }

    @Transactional
    @Override
    public void transferMoney(UUID from, UUID to, BigDecimal amount) {
        log.debug("Transferring {} money from {} to {}", amount, from, to);
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
        Account account = getAccountById(accountRepository::findById, id);
        return accountMapper.toDto(account);
    }

    private Account getAccountById(Function<UUID, Optional<Account>> accountFetcher, UUID id) {
        return accountFetcher.apply(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Account with id [%s] does not exist", id)));
    }
}
