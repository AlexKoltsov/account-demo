package ru.seven.tech.account.web.dto;

import lombok.Data;
import ru.seven.tech.account.data.entity.Person;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AccountDto {
    private UUID id;
    private Person owner;
    private BigDecimal balance;
}
