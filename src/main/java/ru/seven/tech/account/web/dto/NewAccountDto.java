package ru.seven.tech.account.web.dto;

import lombok.Data;
import ru.seven.tech.account.data.entity.Person;

import java.math.BigDecimal;

@Data
public class NewAccountDto {
    private Person owner;
    private BigDecimal balance;
}
