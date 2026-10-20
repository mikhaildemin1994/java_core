package org.management.app.transaction.servise;

import org.management.app.transaction.dto.TransactionCreateDTO;
import org.management.app.transaction.dto.TransactionResponseDTO;
import org.management.app.user.model.User;

public interface TransactionService {
    //Создание черновика транзакции
    TransactionResponseDTO createDraught(TransactionCreateDTO create, User currentUser);

    //Подтверждение транзакции
    TransactionResponseDTO confirmTransaction(Long transactionId);

    //"Откат" транзакции
    TransactionResponseDTO reverseTransaction(Long id, User currentUser);
}
