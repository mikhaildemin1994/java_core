package org.management.app.account.servise;

import org.management.app.account.dto.AccountCreateDTO;
import org.management.app.account.dto.AccountResponseDTO;
import org.management.app.user.model.User;

import java.util.List;

public interface AccountService {
    AccountResponseDTO create(AccountCreateDTO create);
    AccountResponseDTO getById(Long id, User currentUser);
    List<AccountResponseDTO> getAccountsByDepartment(Long departmentId);
}
