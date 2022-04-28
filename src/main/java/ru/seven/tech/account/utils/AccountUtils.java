package ru.seven.tech.account.utils;

import lombok.experimental.UtilityClass;
import ru.seven.tech.account.data.entity.Account;

import java.math.BigDecimal;

@UtilityClass
public class AccountUtils {

    public static void increaseAccountsBalance(Account account, BigDecimal amount) {
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
    }

    public static void decreaseAccountsBalance(Account account, BigDecimal amount) {
        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);
    }
}
