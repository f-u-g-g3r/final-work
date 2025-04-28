package com.artjom.qr_project.DTO;

import com.artjom.qr_project.enums.Role;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String companyName;
    private Long companyId;
    private Role role;
    private String uuid;
    private boolean isActive;
}
