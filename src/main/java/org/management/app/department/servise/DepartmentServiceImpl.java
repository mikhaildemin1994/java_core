package org.management.app.department.servise;

import lombok.RequiredArgsConstructor;
import org.management.app.department.dto.DepartmentRequestDTO;
import org.management.app.department.dto.DepartmentResponseDTO;
import org.management.app.department.model.Department;
import org.management.app.department.repository.DepartmentRepository;
import org.management.app.user.model.User;
import org.management.app.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    @Transactional
    public DepartmentResponseDTO create(DepartmentRequestDTO request) {
        User manager = userRepository.findById(request.managerId()).orElseThrow();

        Department department = new Department();
        department.setName(request.name());
        department.setCode(request.code());
        department.setManager(manager);

        return mapToResponse(departmentRepository.save(department));
    }

    public DepartmentResponseDTO getById(Long id) {
        return departmentRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow();
    }

    public Page<DepartmentResponseDTO> getAll(Pageable pageable) {
        return departmentRepository.findAll(pageable).map(this::mapToResponse);
    }

    public DepartmentResponseDTO mapToResponse(Department department) {
        return new DepartmentResponseDTO(
                department.getId(),
                department.getName(),
                department.getCode(),
                department.getManager().getId()
        );
    }
}
