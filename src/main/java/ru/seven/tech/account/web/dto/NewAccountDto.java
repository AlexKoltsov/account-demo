package ru.seven.tech.account.web.dto;

import lombok.Data;
import ru.seven.tech.account.data.entity.Person;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
public class NewAccountDto {
    @Valid
    private Person owner;

    @PositiveOrZero
    private BigDecimal balance;
}
