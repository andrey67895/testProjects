package unitTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.account.models.Account;
import ru.account.repositories.AccountsRepository;
import ru.account.services.AccountsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountsRepository accountsRepository;
    private AccountsService accountsService;

    @BeforeEach
    private void prepare() {
        accountsService = new AccountsService(accountsRepository);
    }

    @Test
    public void findById() {
        Account account = getAccount();
        when(accountsRepository.findById(any(Long.class))).thenReturn(Optional.of(account));

        Account newAccount = accountsService.getAccountById(123L).orElse(null);

        Assertions.assertNotNull(newAccount);
        Assertions.assertAll(
                () -> Assertions.assertEquals(account.getId(), newAccount.getId()),
                () -> Assertions.assertEquals(account.getFirstName(), newAccount.getFirstName()),
                () -> Assertions.assertEquals(account.getLastName(), newAccount.getLastName())
        );

    }

    @Test
    public void save() {
        Account account = getAccount();
        Account updateAccount = new Account();
        updateAccount.setId(account.getId());
        updateAccount.setFirstName(account.getFirstName() + "_UPDATE");
        updateAccount.setLastName(account.getLastName() + "_UPDATE");
        when(accountsRepository.save(any(Account.class))).thenReturn(updateAccount);


        Account newAccount = accountsService.saveAccount(updateAccount);

        Assertions.assertNotNull(newAccount);
        Assertions.assertAll(
                () -> Assertions.assertEquals(updateAccount.getId(), newAccount.getId()),
                () -> Assertions.assertEquals(updateAccount.getFirstName(), newAccount.getFirstName()),
                () -> Assertions.assertEquals(updateAccount.getLastName(), newAccount.getLastName())
        );

    }

    @Test
    public void getAccounts() {
        Account first = getAccount();
        Account second = getAccount();
        second.setId(124L);
        List<Account> listAccount = new ArrayList<>();
        listAccount.add(first);
        listAccount.add(second);
        when(accountsRepository.getAccounts(any(PageRequest.class))).thenReturn(listAccount);

        List<Account> newListAccount = accountsService.getAccounts(0, 2);
        for (int i = 0; i < newListAccount.size(); i++) {
            Account newAccount = newListAccount.get(i);
            Account account = listAccount.get(i);
            Assertions.assertAll(
                    () -> Assertions.assertEquals(account.getId(), newAccount.getId()),
                    () -> Assertions.assertEquals(account.getFirstName(), newAccount.getFirstName()),
                    () -> Assertions.assertEquals(account.getLastName(), newAccount.getLastName())
            );
        }
        }

    @Test
    public void deleteById() {
        Account account = getAccount();
        Assertions.assertDoesNotThrow(() -> accountsService.deleteById(account.getId()));
    }

    private Account getAccount() {
        return Account
                .builder()
                .id(123L)
                .firstName("TEST_FIRST_NAME")
                .lastName("TEST_LAST_NAME")
                .build();
    }
}
