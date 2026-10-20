package org.management.app.transaction.servise;

import lombok.RequiredArgsConstructor;
import org.management.app.account.model.Account;
import org.management.app.account.repository.AccountRepository;
import org.management.app.auditlog.aspect.AuditAnnotation;
import org.management.app.auditlog.model.AuditLog;
import org.management.app.transaction.dto.TransactionCreateDTO;
import org.management.app.transaction.dto.TransactionResponseDTO;
import org.management.app.transaction.model.Transaction;
import org.management.app.transaction.repository.TransactionRepository;
import org.management.app.user.model.User;
import org.management.app.util.enums.TransactionStatus;
import org.management.app.util.enums.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    @AuditAnnotation(action = AuditLog.Action.CREATION)
    public TransactionResponseDTO createDraught(TransactionCreateDTO create, User currentUser) {
        Account accountFrom = accountRepository.findById(create.accountFromId())
                .orElseThrow();

        Transaction transaction = new Transaction();
        transaction.setType(create.type());
        transaction.setAmount(create.amount());
        transaction.setCurrency(create.currency().toUpperCase());
        transaction.setDate(create.date());
        transaction.setAccountFrom(accountFrom);
        transaction.setStatus(TransactionStatus.DRAUGHT);
        transaction.setUser(currentUser);

        if (create.type() == TransactionType.TRANSFER) {
            Account accountTo = accountRepository.findById(create.accountToId())
                    .orElseThrow();

            transaction.setAccountTo(accountTo);
        }

        return mapToResponse(transactionRepository.save(transaction));
    }

    @Transactional
    @AuditAnnotation(action = AuditLog.Action.CONFIRMATION)
    public TransactionResponseDTO confirmTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow();

        if (transaction.getStatus() == TransactionStatus.CONFIRMED) {
            throw new IllegalStateException("Only DRAFT transactions can be confirmed");
        }

        // Пессимистическая блокировка счетов в БД для безопасного обновления баланса
        Account accountFrom = accountRepository
                .findByIdForUpdate(transaction.getAccountFrom().getId())
                .orElseThrow();

        if (transaction.getType() == TransactionType.INCOME) {
            accountFrom.income(transaction.getAmount());
        } else if (transaction.getType() == TransactionType.EXPENDITURE) {
            accountFrom.expense(transaction.getAmount());
        } else if (transaction.getType() == TransactionType.TRANSFER) {
            Account accountTo = accountRepository
                    .findByIdForUpdate(transaction.getAccountTo().getId())
                    .orElseThrow();

            accountFrom.expense(transaction.getAmount());
            accountTo.income(transaction.getAmount());
        }

        transaction.setStatus(TransactionStatus.CONFIRMED);
        return mapToResponse(transaction);
    }

    @Transactional
    @AuditAnnotation(action = AuditLog.Action.REVERSE)
    public TransactionResponseDTO reverseTransaction(Long id, User currentUser) {
        Transaction originalTransaction = transactionRepository.findById(id)
                .orElseThrow();

        Account accountFrom = accountRepository
                .findByIdForUpdate(originalTransaction.getAccountFrom().getId())
                .orElseThrow();

        if (originalTransaction.getType() == TransactionType.INCOME) {
            accountFrom.expense(originalTransaction.getAmount());
        } else if (originalTransaction.getType() == TransactionType.EXPENDITURE) {
            accountFrom.income(originalTransaction.getAmount());
        } else if (originalTransaction.getType() == TransactionType.TRANSFER) {
            Account accountTo = accountRepository
                    .findByIdForUpdate(originalTransaction.getAccountTo().getId())
                    .orElseThrow();

            accountTo.expense(originalTransaction.getAmount());
            accountFrom.income(originalTransaction.getAmount());
        }

        originalTransaction.setStatus(TransactionStatus.REVERSED);

        Transaction reverseTransaction = new Transaction();
        reverseTransaction.setType(originalTransaction.getType());
        reverseTransaction.setAmount(originalTransaction.getAmount().negate());
        reverseTransaction.setCurrency(originalTransaction.getCurrency());
        reverseTransaction.setDate(LocalDateTime.now());
        reverseTransaction.setAccountFrom(originalTransaction.getAccountFrom());
        reverseTransaction.setAccountTo(originalTransaction.getAccountTo());
        reverseTransaction.setStatus(TransactionStatus.REVERSED);
        reverseTransaction.setUser(currentUser);

        transactionRepository.save(reverseTransaction);
        return mapToResponse(reverseTransaction);
    }

    private TransactionResponseDTO mapToResponse(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getDate(),
                transaction.getAccountFrom() != null ? transaction.getAccountFrom().getId() : null,

                transaction.getAccountTo() != null ? transaction.getAccountTo().getId() : null,

                (transaction.getCategory() != null && transaction.getCategory().getParentCategory() != null)
                        ? transaction.getCategory().getParentCategory().getId() : null,

                transaction.getCounterparty() != null ? transaction.getCounterparty().getId() : null,

                transaction.getStatus(),
                transaction.getUser() != null ? transaction.getUser().getId() : null
        );
    }
}
