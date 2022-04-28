package ru.seven.tech.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.seven.tech.account.data.entity.Account;
import ru.seven.tech.account.data.entity.Person;
import ru.seven.tech.account.data.repo.AccountRepository;
import ru.seven.tech.account.expcetions.EntityNotFoundException;
import ru.seven.tech.account.web.dto.AccountDto;
import ru.seven.tech.account.web.dto.NewAccountDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountsServiceImplTest {

    @Autowired
    private AccountsServiceImpl accountsService;
    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        accountRepository.flush();
    }

    @Test
    void openNewAccount() {
        accountsService.openNewAccount(getNewAccountDto());

        List<Account> accounts = accountRepository.findAll();
        assertAll(
                () -> assertEquals(1, accounts.size()),
                () -> assertEquals("100000.00", normalize(accounts.get(0).getBalance())),
                () -> assertEquals("New", accounts.get(0).getOwner().getFirstName()),
                () -> assertEquals("Person", accounts.get(0).getOwner().getLastName()),
                () -> assertEquals(30, accounts.get(0).getOwner().getAge())
        );
    }

    @Test
    void depositMoney() {
        Account saved = accountRepository.save(getNewAccount());
        BigDecimal amountToAdd = new BigDecimal(10_000);
        BigDecimal expectedBalance = new BigDecimal(110_000);

        accountsService.depositMoney(saved.getId(), amountToAdd);
        Account account = accountRepository.findById(saved.getId()).orElseThrow();

        assertAll(
                () -> assertNotNull(account),
                () -> assertEquals(normalize(expectedBalance), normalize(account.getBalance()))
        );
    }


    @Test
    void withdrawMoney() {
        Account saved = accountRepository.save(getNewAccount());
        BigDecimal amountToWithdraw = new BigDecimal(10_000);
        BigDecimal expectedBalance = new BigDecimal(90_000);

        accountsService.withdrawMoney(saved.getId(), amountToWithdraw);
        Account account = accountRepository.findById(saved.getId()).orElseThrow();

        assertAll(
                () -> assertNotNull(account),
                () -> assertEquals(normalize(expectedBalance), normalize(account.getBalance()))
        );
    }

    @Test
    void transferMoney() {
        Account account1 = accountRepository.save(getNewAccount());
        Account account2 = accountRepository.save(getNewAccount());
        BigDecimal amountToTransfer = new BigDecimal(10_000);
        BigDecimal expectedBalance1 = new BigDecimal(90_000);
        BigDecimal expectedBalance2 = new BigDecimal(110_000);

        accountsService.transferMoney(account1.getId(), account2.getId(), amountToTransfer);
        Account foundAccount1 = accountRepository.findById(account1.getId()).orElseThrow();
        Account foundAccount2 = accountRepository.findById(account2.getId()).orElseThrow();

        assertAll(
                () -> assertNotNull(foundAccount1),
                () -> assertNotNull(foundAccount2),
                () -> assertEquals(normalize(expectedBalance1), normalize(foundAccount1.getBalance())),
                () -> assertEquals(normalize(expectedBalance2), normalize(foundAccount2.getBalance()))
        );
    }

    @Test
    void getAll() {
        accountRepository.save(getNewAccount());
        accountRepository.save(getNewAccount());
        accountRepository.save(getNewAccount());
        accountRepository.save(getNewAccount());
        accountRepository.save(getNewAccount());

        List<AccountDto> accounts = accountsService.getAll();

        assertEquals(5, accounts.size());
    }

    @Test
    void getById() {
        Account saved = accountRepository.save(getNewAccount());

        AccountDto found = accountsService.getById(saved.getId());

        assertNotNull(found);
    }

    @Test
    void getByWrongId() {
        accountRepository.save(getNewAccount());

        assertThrows(EntityNotFoundException.class, () -> accountsService.getById(UUID.randomUUID()));
    }

    @Test
    void transferMoneyStressTest() throws InterruptedException {
        int numberOfTransactions = 10_000;
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Account account1 = accountRepository.save(getNewAccount());
        Account account2 = accountRepository.save(getNewAccount());
        BigDecimal amountToTransfer = new BigDecimal(1);

        for (int i = 0; i < numberOfTransactions; i++) {
            executorService.submit(() -> accountsService.transferMoney(account1.getId(), account2.getId(), amountToTransfer));
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);


        BigDecimal expectedBalance1 = new BigDecimal(100_000 - numberOfTransactions);
        BigDecimal expectedBalance2 = new BigDecimal(100_000 + numberOfTransactions);

        Account foundAccount1 = accountRepository.findById(account1.getId()).orElseThrow();
        Account foundAccount2 = accountRepository.findById(account2.getId()).orElseThrow();

        assertAll(
                () -> assertNotNull(foundAccount1),
                () -> assertNotNull(foundAccount2),
                () -> assertEquals(normalize(expectedBalance1), normalize(foundAccount1.getBalance())),
                () -> assertEquals(normalize(expectedBalance2), normalize(foundAccount2.getBalance()))
        );
    }

    private NewAccountDto getNewAccountDto() {
        return new NewAccountDto(getOwner(), new BigDecimal(100_000));
    }

    private Account getNewAccount() {
        Account account = new Account();
        account.setOwner(getOwner());
        account.setBalance(new BigDecimal(100_000));
        return account;
    }

    private Person getOwner() {
        return new Person("New", "Person", 30);
    }

    private String normalize(BigDecimal balance) {
        return balance.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}