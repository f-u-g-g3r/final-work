package com.artjom.qr_project.DTO.mapper;

import com.artjom.qr_project.DTO.CompanyDTO;
import com.artjom.qr_project.model.Company;

public class CompanyMapper {
    public static CompanyDTO toDTO(Company company) {
        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setCompanyEmail(company.getCompanyEmail());
        dto.setManagerEmail(company.getManagerEmail());
        dto.setRegistrationCode(company.getRegistrationCode());
        dto.setEnabled(company.isEnabled());
        return dto;
    }
}
