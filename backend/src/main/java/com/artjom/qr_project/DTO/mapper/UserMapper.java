package com.artjom.qr_project.DTO.mapper;

import com.artjom.qr_project.DTO.UserDTO;
import com.artjom.qr_project.model.User;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getCompany() == null ? null : user.getCompany().getName(), user.getCompany() == null ? null : user.getCompany().getId(), user.getRole(), user.getUuid(), user.isActive());
    }
}
