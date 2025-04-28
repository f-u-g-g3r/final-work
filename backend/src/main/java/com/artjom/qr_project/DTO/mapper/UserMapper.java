package com.artjom.qr_project.DTO.mapper;

import com.artjom.qr_project.DTO.UserDTO;
import com.artjom.qr_project.model.User;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setCompanyName(user.getCompany() == null ? null : user.getCompany().getName());
        dto.setCompanyId(user.getCompany() == null ? null : user.getCompany().getId());
        dto.setActive(user.isActive());
        dto.setRole(user.getRole());
        dto.setUuid(user.getUuid());
        return dto;
    }
}
