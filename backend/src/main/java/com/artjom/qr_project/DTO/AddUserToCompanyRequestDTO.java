package com.artjom.qr_project.DTO;

import lombok.Data;

@Data
public class AddUserToCompanyRequestDTO {
    private Long companyId;
    private String userUniqId;
}
