package com.artjom.qr_project.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyDTO {
    private Long id;
    private String name;
    private String companyEmail;
    private String managerEmail;
    private String registrationCode;

    private boolean isEnabled;
}
