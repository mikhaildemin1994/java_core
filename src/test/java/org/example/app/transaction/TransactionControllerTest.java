package org.example.app.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.management.app.account.model.Account;
import org.management.app.account.repository.AccountRepository;
import org.management.app.category.model.Category;
import org.management.app.counterparty.model.Counterparty;
import org.management.app.transaction.dto.TransactionResponseDTO;
import org.management.app.transaction.model.Transaction;
import org.management.app.transaction.repository.TransactionRepository;
import org.management.app.transaction.servise.TransactionServiceImpl;
import org.management.app.user.model.User;
import org.management.app.util.enums.TransactionStatus;
import org.management.app.util.enums.TransactionType;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private User mockUser;
    private Account mockAccountFrom;
    private Account mockAccountTo;
    private Category mockCategory;
    private Counterparty mockCounterparty;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);

        Category parentCategory = new Category();
        parentCategory.setId(100L);
        mockCategory = new Category();
        mockCategory.setId(10L);
        mockCategory.setParentCategory(parentCategory);

        mockCounterparty = new Counterparty();
        mockCounterparty.setId(20L);

        mockAccountFrom = spy(new Account());
        mockAccountFrom.setId(1L);

        mockAccountTo = spy(new Account());
        mockAccountTo.setId(2L);
    }

    @Test
    @DisplayName("Негативный сценарий: " +
            "Попытка подтверждения уже подтвержденной транзакции должна вызывать ошибку")
    void confirmTransaction_WhenAlreadyConfirmed_ShouldThrowException() {
        // Given
        Long transactionId = 99L;
        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setStatus(TransactionStatus.CONFIRMED);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            transactionService.confirmTransaction(transactionId);
        });

        assertEquals("Only DRAFT transactions can be confirmed", exception.getMessage());
        verify(accountRepository, never()).findByIdForUpdate(anyLong());
    }

    @Test
    @DisplayName("Позитивный сценарий: " +
            "Сторнирование подтвержденного расхода должно корректно вернуть средства " +
            "и создать реверс-транзакцию")
    void reverseTransaction_Expenditure_ShouldCorrectlyRestoreBalanceAndCreateReverseRecord() {
        // Given
        Long transactionId = 55L;
        BigDecimal transactionAmount = new BigDecimal("1500.00");

        Transaction reverseTransaction = new Transaction();
        reverseTransaction.setId(transactionId);
        reverseTransaction.setType(TransactionType.EXPENDITURE);
        reverseTransaction.setAmount(transactionAmount);
        reverseTransaction.setCurrency("RUB");
        reverseTransaction.setAccountFrom(mockAccountFrom);
        reverseTransaction.setAccountTo(mockAccountTo);
        reverseTransaction.setCategory(mockCategory);
        reverseTransaction.setCounterparty(mockCounterparty);
        reverseTransaction.setStatus(TransactionStatus.CONFIRMED);
        reverseTransaction.setUser(mockUser);

        when(transactionRepository
                .findById(transactionId))
                .thenReturn(Optional.of(reverseTransaction));
        when(accountRepository
                .findByIdForUpdate(mockAccountFrom.getId()))
                .thenReturn(Optional.of(mockAccountFrom));

        // When
        TransactionResponseDTO response =
                transactionService.reverseTransaction(transactionId, mockUser);

        // Then
        verify(mockAccountFrom, times(1)).income(transactionAmount);
        assertEquals(TransactionStatus.REVERSED, reverseTransaction.getStatus());

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, times(1))
                .save(transactionCaptor.capture());

        Transaction savedReverse = transactionCaptor.getValue();
        assertEquals(TransactionType.EXPENDITURE, savedReverse.getType());
        assertEquals(transactionAmount.negate(), savedReverse.getAmount());
        assertEquals(TransactionStatus.REVERSED, savedReverse.getStatus());
        assertEquals(mockUser, savedReverse.getUser());
    }

    @Test
    @DisplayName("Позитивный сценарий: " +
            "Сторнирование перевода (TRANSFER) должно атомарно списать деньги с получателя " +
            "и вернуть отправителю")
    void reverseTransaction_Transfer_ShouldAtomicallyRevertBothAccounts() {
        // Given
        Long transactionId = 77L;
        BigDecimal transferAmount = new BigDecimal("5000.00");

        Transaction reverseTransaction = new Transaction();
        reverseTransaction.setId(transactionId);
        reverseTransaction.setType(TransactionType.TRANSFER);
        reverseTransaction.setAmount(transferAmount);
        reverseTransaction.setCurrency("USD");
        reverseTransaction.setAccountFrom(mockAccountFrom);
        reverseTransaction.setAccountTo(mockAccountTo);
        reverseTransaction.setCategory(mockCategory);
        reverseTransaction.setCounterparty(mockCounterparty);
        reverseTransaction.setStatus(TransactionStatus.CONFIRMED);
        reverseTransaction.setUser(mockUser);

        when(transactionRepository
                .findById(transactionId))
                .thenReturn(Optional.of(reverseTransaction));
        when(accountRepository
                .findByIdForUpdate(mockAccountFrom.getId()))
                .thenReturn(Optional.of(mockAccountFrom));
        when(accountRepository
                .findByIdForUpdate(mockAccountTo.getId()))
                .thenReturn(Optional.of(mockAccountTo));

        // When
        transactionService.reverseTransaction(transactionId, mockUser);

        // Then
        verify(mockAccountFrom, times(1)).income(transferAmount);
        verify(mockAccountTo, times(1)).expense(transferAmount);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, times(1))
                .save(transactionCaptor.capture());

        Transaction savedReverse = transactionCaptor.getValue();
        assertEquals(transferAmount.negate(), savedReverse.getAmount());
        assertEquals(mockAccountFrom, savedReverse.getAccountFrom());
        assertEquals(mockAccountTo, savedReverse.getAccountTo());
    }
}
