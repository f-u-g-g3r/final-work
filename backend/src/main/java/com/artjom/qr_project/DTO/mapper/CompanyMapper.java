package com.artjom.qr_project.DTO.mapper;

import com.artjom.qr_project.DTO.CompanyDTO;
import com.artjom.qr_project.model.Company;

public class CompanyMapper {
    public static CompanyDTO toDTO(Company company) {
        return new CompanyDTO(company.getId(), company.getName(), company.getCompanyEmail(), company.getManagerEmail(), company.getRegistrationCode(), company.isEnabled());
    }
}
