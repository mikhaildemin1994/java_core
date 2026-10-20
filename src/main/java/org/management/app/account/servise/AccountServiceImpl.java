package org.management.app.account.servise;

import lombok.RequiredArgsConstructor;
import org.management.app.account.dto.AccountCreateDTO;
import org.management.app.account.dto.AccountResponseDTO;
import org.management.app.account.model.Account;
import org.management.app.account.repository.AccountRepository;
import org.management.app.department.model.Department;
import org.management.app.department.repository.DepartmentRepository;
import org.management.app.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final DepartmentRepository departmentRepository;

    public AccountResponseDTO create(AccountCreateDTO create) {
        Department department = departmentRepository.findById(create.departmentId())
                .orElseThrow();

        Account account = new Account();
        account.setType(create.accountType());
        account.setCurrency(create.currency().toUpperCase());
        account.setDepartment(department);

        return mapToResponse(accountRepository.save(account));
    }

    public AccountResponseDTO getById(Long id, User currentUser) {
        Account account = accountRepository.findById(id)
                .orElseThrow();

        return mapToResponse(account);
    }

    public List<AccountResponseDTO> getAccountsByDepartment(Long departmentId) {
        return accountRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    public AccountResponseDTO mapToResponse(Account account) {
        return new AccountResponseDTO(
                account.getId(),
                account.getType(),
                account.getCurrency(),
                account.getBalance(),
                account.getDepartment().getId()
        );
    }
}
