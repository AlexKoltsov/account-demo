package ru.seven.tech.account.data.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.seven.tech.account.data.entity.Account;

import javax.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select a  from Account a where a.id = :uuid")
    Optional<Account> findByIdForUpdate(UUID uuid);
}
