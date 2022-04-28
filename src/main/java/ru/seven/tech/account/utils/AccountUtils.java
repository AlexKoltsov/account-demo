package ru.seven.tech.account.utils;

import lombok.experimental.UtilityClass;
import ru.seven.tech.account.data.entity.Account;
import ru.seven.tech.account.expcetions.NegativeBalanceException;

import java.math.BigDecimal;

@UtilityClass
public class AccountUtils {

    public static void increaseAccountsBalance(Account account, BigDecimal amount) {
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
    }

    public static void decreaseAccountsBalance(Account account, BigDecimal amount) {
        BigDecimal newBalance = account.getBalance().subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeBalanceException(String.format("Balance of [%s] account could not be negative", account.getId()));
        }
        account.setBalance(newBalance);
    }
}
