package integrationTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.account.Application;
import ru.account.models.Account;
import ru.account.services.AccountsService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Testcontainers
@SpringBootTest(classes = Application.class)
@ContextConfiguration(initializers = {AccountServiceIntegrationTest.Initializer.class})
public class AccountServiceIntegrationTest {

    @Autowired
    private AccountsService accountsService;
    @PersistenceContext
    private EntityManager entityManager;

    @Container
    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:12")
           .withDatabaseName("integration-tests-db")
            .withUsername("test")
           .withPassword("test");

    @BeforeAll
    static void healthCheck() {
        Assertions.assertTrue(postgreSQLContainer.isRunning());
    }

    @Test
    public void saveAccount() {
        Account account = new Account();
        account.setLastName("TEST");
        account.setFirstName("TEST");
        Account newAccount = accountsService.saveAccount(account);
        account.setId(newAccount.getId());
        checkAccount(account, newAccount);
    }

    @Test
    public void getAccountById() {
        Account account = createdAccount();
        Account getAccountById = accountsService.getAccountById(account.getId()).orElse(null);
        Assertions.assertNotNull(getAccountById);
        checkAccount(account, getAccountById);
    }

    @Test
    public void deleteById() {
        Account account = createdAccount();
        accountsService.deleteById(account.getId());
        Account getAccountById = accountsService.getAccountById(account.getId()).orElse(null);
        Assertions.assertNull(getAccountById);
    }

    private Account createdAccount() {
        Account account = new Account();
        account.setLastName("TEST");
        account.setFirstName("TEST");
        return accountsService.saveAccount(account);
    }

    private void checkAccount(Account expected, Account actual) {
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getId(), actual.getId()),
                () -> Assertions.assertEquals(expected.getLastName(), actual.getLastName()),
                () -> Assertions.assertEquals(expected.getFirstName(), actual.getFirstName())
        );
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.driver-class-name=" + postgreSQLContainer.getDriverClassName(),
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }


}
