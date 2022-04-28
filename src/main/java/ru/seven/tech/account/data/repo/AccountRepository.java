package ru.seven.tech.account.data.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.seven.tech.account.data.entity.Account;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
}
