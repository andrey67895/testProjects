package ru.account.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.account.models.Account;
import ru.account.repositories.AccountsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AccountsService {
    private final AccountsRepository accountsRepository;

    public AccountsService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public List<Account> getAccounts(Integer offset, Integer limit){
        return accountsRepository.getAccounts(PageRequest.of(offset, limit));
    }

    public Optional<Account> getAccountById(Long id){
        return accountsRepository.findById(id);
    }

    @Transactional
    @Modifying
    public Account saveAccount(Account account){
        return accountsRepository.save(account);
    }

    @Transactional
    @Modifying
    public void deleteById(Long id){
        accountsRepository.deleteById(id);
    }
}
