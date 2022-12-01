package ru.account.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.account.models.Account;
import java.util.List;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Long> {
    @Query(value = "SELECT * FROM account", nativeQuery = true)
    List<Account> getAccounts(Pageable pageable);
}
