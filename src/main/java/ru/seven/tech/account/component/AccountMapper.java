package ru.seven.tech.account.component;

import org.springframework.stereotype.Component;
import ru.seven.tech.account.data.entity.Account;
import ru.seven.tech.account.web.dto.AccountDto;
import ru.seven.tech.account.web.dto.NewAccountDto;

import java.math.RoundingMode;


@Component
public class AccountMapper {
    public AccountDto toDto(Account entity) {
        AccountDto dto = new AccountDto();
        dto.setId(entity.getId());
        dto.setOwner(entity.getOwner());
        dto.setBalance(entity.getBalance().setScale(2, RoundingMode.HALF_UP));
        return dto;
    }

    public Account toEntity(NewAccountDto newAccountDto) {
        Account account = new Account();
        account.setOwner(newAccountDto.getOwner());
        account.setBalance(newAccountDto.getBalance());
        return account;
    }
}
