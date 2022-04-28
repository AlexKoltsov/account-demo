package ru.seven.tech.account.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.seven.tech.account.data.entity.Person;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewAccountDto {
    @Valid
    private Person owner;

    @PositiveOrZero
    private BigDecimal balance;
}
