package com.artjom.qr_project.repository;

import com.artjom.qr_project.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByCompanyEmail(String email);
    boolean existsByManagerEmail(String managerEmail);
    boolean existsByName(String name);
    boolean existsByRegistrationCode(String registrationCode);

    Page<Company> findAllByIsEnabled(boolean enabled, Pageable pageable);
    List<Company> findAllByIsEnabled(boolean enabled);

    Page<Company> findAllByNameContainingIgnoreCaseOrCompanyEmailContainingIgnoreCaseOrManagerEmailContainingIgnoreCaseOrRegistrationCodeContainingIgnoreCase(
            String name, String companyEmail, String managerEmail, String registrationCode, Pageable pageable);

}
