package org.management.app.department.servise;

import org.management.app.department.dto.DepartmentRequestDTO;
import org.management.app.department.dto.DepartmentResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {
    DepartmentResponseDTO create(DepartmentRequestDTO request);
    DepartmentResponseDTO getById(Long id);
    Page<DepartmentResponseDTO> getAll(Pageable pageable);
}
