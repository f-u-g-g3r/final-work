package com.artjom.qr_project.DTO;

public record CompanyDTO(Long id, String name, String companyEmail, String managerEmail, String registrationCode, boolean isEnabled) {
}
