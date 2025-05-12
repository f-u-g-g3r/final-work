package com.artjom.qr_project.DTO;

import com.artjom.qr_project.enums.Role;

public record UserDTO(Long id, String name, String email, String companyName, Long companyId, Role role, String uuid, boolean isActive) {
}
