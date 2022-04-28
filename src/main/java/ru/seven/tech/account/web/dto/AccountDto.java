package ru.seven.tech.account.web.dto;

import lombok.Data;
import ru.seven.tech.account.data.entity.Person;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AccountDto {

    @NotNull
    private UUID id;

    @Valid
    private Person owner;

    @PositiveOrZero
    private BigDecimal balance;
}
