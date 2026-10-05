package banksystem.service;

import banksystem.Dto.BankAccountDTO;
import banksystem.Dto.UserDTO;
import banksystem.Entity.BankAccount;
import banksystem.Entity.User;
import banksystem.Enum.Role;
import banksystem.Repository.BankAccountRepository;
import banksystem.Repository.UserRepository;
import banksystem.Service.BankAccountService;
import banksystem.Service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static banksystem.Enum.Role.CLIENT;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BankAccountServiceTest {
    @Test
    void createAccountSuccess() {
        UserRepository userRepository = mock(UserRepository.class);
        BankAccountRepository bankAccountRepository = mock(BankAccountRepository.class);
        TransactionService transactionService = mock(TransactionService.class);
        BankAccountService bankAccountService = new BankAccountService(bankAccountRepository, userRepository, transactionService);
        User user = new User(
                "example@gmail.com",
                "test-password",
                Role.USER
        );
        user.setId(1L);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(bankAccountRepository.save(any(BankAccount.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        BankAccountDTO add = bankAccountService.createAccount(user.getId());
        assertEquals("example@gmail.com", add.userEmail());

    }
    @Test
    void createAccountClient() {
        UserRepository userRepository = mock(UserRepository.class);
        BankAccountRepository bankAccountRepository = mock(BankAccountRepository.class);
        TransactionService transactionService = mock(TransactionService.class);
        BankAccountService bankAccountService = new BankAccountService(bankAccountRepository, userRepository, transactionService);
        User user = new User(
                "example@gmail.com",
                "test-password",
                CLIENT
        );
        user.setId(1L);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        assertThrows(
            IllegalStateException.class,
                () -> bankAccountService.createAccount(user.getId())
        );
    }
    @Test
    void findById() {
        UserRepository userRepository = mock(UserRepository.class);
        BankAccountRepository bankAccountRepository = mock(BankAccountRepository.class);
        TransactionService transactionService = mock(TransactionService.class);
        BankAccountService bankAccountService = new BankAccountService(bankAccountRepository, userRepository, transactionService);
        User user = new User("email@example.com", "test-password", CLIENT);
        user.setId(1L);
        BankAccount bankAccount= new BankAccount(
                user, 23333L
        );
        when(bankAccountRepository.findById(1L))
                .thenReturn(Optional.of(bankAccount));
        BankAccountDTO bankAccountDTO = bankAccountService.findById(1L);
        assertEquals(23333L, bankAccountDTO.accountNumber());
        assertEquals("email@example.com", bankAccountDTO.userEmail());
    }
    @Test
    void findByIdNotFound() {
        UserRepository userRepository = mock(UserRepository.class);
        BankAccountRepository bankAccountRepository = mock(BankAccountRepository.class);
        TransactionService transactionService = mock(TransactionService.class);
        BankAccountService bankAccountService = new BankAccountService(bankAccountRepository, userRepository, transactionService);
        when(bankAccountRepository.findById(1L))
                .thenReturn(Optional.empty());
        assertThrows(
                EntityNotFoundException.class,
                () -> bankAccountService.findById(1L)
        );
    }

    @Test
    void transferSuccess() {
        UserRepository userRepository = mock(UserRepository.class);
        BankAccountRepository bankAccountRepository = mock(BankAccountRepository.class);
        TransactionService transactionService = mock(TransactionService.class);
        BankAccountService bankAccountService = new BankAccountService(bankAccountRepository, userRepository, transactionService);

        User senderUser = new User("sender@example.com", "password", CLIENT);
        User receiverUser = new User("receiver@example.com", "password", CLIENT);
        BankAccount sender = new BankAccount(senderUser, 11111L);
        BankAccount receiver = new BankAccount(receiverUser, 22222L);
        sender.setId(1L);
        receiver.setId(2L);
        sender.deposit(new BigDecimal("100.00"));

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(bankAccountRepository.findById(2L)).thenReturn(Optional.of(receiver));

        BigDecimal amount = new BigDecimal("25.00");
        BankAccountDTO result = bankAccountService.transfer(1L, 2L, amount);

        assertEquals(new BigDecimal("75.00"), sender.getBalance());
        assertEquals(new BigDecimal("25.00"), receiver.getBalance());
        assertEquals(11111L, result.accountNumber());
        assertEquals("sender@example.com", result.userEmail());
    }

    @Test
    void transferToSameAccountThrowsException() {
        UserRepository userRepository = mock(UserRepository.class);
        BankAccountRepository bankAccountRepository = mock(BankAccountRepository.class);
        TransactionService transactionService = mock(TransactionService.class);
        BankAccountService bankAccountService = new BankAccountService(bankAccountRepository, userRepository, transactionService);

        User user = new User("user@example.com", "password", CLIENT);
        BankAccount account = new BankAccount(user, 11111L);
        account.setId(1L);
        account.deposit(new BigDecimal("100.00"));

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(
                IllegalStateException.class,
                () -> bankAccountService.transfer(1L, 1L, new BigDecimal("25.00"))
        );

        assertEquals(new BigDecimal("100.00"), account.getBalance());
    }
}
