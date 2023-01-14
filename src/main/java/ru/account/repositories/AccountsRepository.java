package ru.account.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.account.models.Account;
import java.util.List;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Long> {
    @Query(value = "SELECT * FROM account", nativeQuery = true)
    List<Account> getAccounts(Pageable pageable);

    @Query(value = "SELECT * FROM account WHERE first_name like '%:text%' OR last_name like '%:text%' ", nativeQuery = true)
    List<Account> getAccountsBySearchText(@Param("text") String text, Pageable pageable);
}
