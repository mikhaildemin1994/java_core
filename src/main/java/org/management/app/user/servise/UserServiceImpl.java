package org.management.app.user.servise;

import lombok.RequiredArgsConstructor;
import org.management.app.user.dto.UserResponseDTO;
import org.management.app.user.model.User;
import org.management.app.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserResponseDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::mapToResponse)
                .orElseThrow();
    }

    public UserResponseDTO mapToResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUserRole(),
                user.getEmail(),
                user.getDepartment().getId()
        );
    }
}
