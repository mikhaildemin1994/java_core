package org.management.app.user.servise;

import org.management.app.user.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO getUserByEmail(String email);
}
