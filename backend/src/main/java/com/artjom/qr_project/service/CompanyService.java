package com.artjom.qr_project.service;

import com.artjom.qr_project.DTO.AddUserToCompanyRequestDTO;
import com.artjom.qr_project.DTO.CompanyDTO;
import com.artjom.qr_project.DTO.FilterRequest;
import com.artjom.qr_project.DTO.mapper.CompanyMapper;
import com.artjom.qr_project.assembler.CompanyModelAssembler;
import com.artjom.qr_project.controller.CompanyController;
import com.artjom.qr_project.enums.Role;
import com.artjom.qr_project.exception.CompanyNotFoundException;
import com.artjom.qr_project.exception.UserNotFoundException;
import com.artjom.qr_project.model.Company;
import com.artjom.qr_project.model.User;
import com.artjom.qr_project.repository.CompanyRepository;
import com.artjom.qr_project.repository.UserRepository;
import com.artjom.qr_project.security.customUserDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyModelAssembler companyModelAssembler;

    public CollectionModel<EntityModel<CompanyDTO>> findAll() {
        List<EntityModel<CompanyDTO>> companies = companyRepository.findAll().stream()
                .map(CompanyMapper::toDTO)
                .map(companyModelAssembler::toModel)
                .toList();

        return CollectionModel.of(companies, linkTo(methodOn(CompanyController.class).all()).withSelfRel());
    }

    public EntityModel<CompanyDTO> findById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(id));
        return companyModelAssembler.toModel(CompanyMapper.toDTO(company));
    }

    public ResponseEntity<?> add(CompanyDTO company) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            User user = userRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new UserNotFoundException(customUserDetails.getId()));

            if (!isManagerEmailValid(user.getEmail())) {
                return ResponseEntity.badRequest().body("Manager email is not valid");
            }

            Company newCompany = new Company(company.name(), company.companyEmail(), user.getEmail(), company.registrationCode());

            newCompany.setCreatedAt(LocalDateTime.now());
            Company companyWithoutUser = companyRepository.save(newCompany);

            user.setCompany(companyWithoutUser);
            user.setRole(Role.COMPANY_OWNER);
            userRepository.save(user);
            companyWithoutUser.addUser(user);
            Company savedCompany = companyRepository.save(companyWithoutUser);

            return ResponseEntity.created(linkTo(methodOn(CompanyController.class).one(savedCompany.getId())).toUri())
                    .body(companyModelAssembler.toModel(CompanyMapper.toDTO(savedCompany)));
        }
        return ResponseEntity.badRequest().body("Request is not valid");
    }


    private boolean isManagerEmailValid(String managerEmail) {
        return userRepository.existsByEmail(managerEmail) && !companyRepository.existsByManagerEmail(managerEmail);
    }

    public ResponseEntity<?> activateCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(id));

        company.setEnabled(true);
        companyRepository.save(company);
        User user = userRepository.findByEmail(company.getManagerEmail())
                .orElseThrow(() -> new UserNotFoundException(company.getManagerEmail()));
        user.setRole(Role.COMPANY_OWNER);
        user.setActive(true);
        userRepository.save(user);

        return ResponseEntity.ok(companyModelAssembler.toModel(CompanyMapper.toDTO(company)));
    }

    public ResponseEntity<?> deactivateCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(id));

        company.setEnabled(false);
        companyRepository.save(company);
        User user = userRepository.findByEmail(company.getManagerEmail())
                .orElseThrow(() -> new UserNotFoundException(company.getManagerEmail()));
        user.setRole(Role.DISABLED_USER);
        user.setActive(false);
        userRepository.save(user);

        return ResponseEntity.ok(companyModelAssembler.toModel(CompanyMapper.toDTO(company)));
    }

    public Page<CompanyDTO> findWithFilter(FilterRequest filterRequest) {
        Pageable pageable = PageRequest.of(
                filterRequest.getPagingOptions().page,
                filterRequest.getPagingOptions().pageSize,
                filterRequest.getSortOptions().direction == 0 ? Sort.by(filterRequest.getSortOptions().sortBy).ascending()
                        : Sort.by(filterRequest.getSortOptions().sortBy).descending());

        if (filterRequest.getFilterOptions() != null) {
            for (FilterRequest.FilterOptions filterOption : filterRequest.getFilterOptions()) {
                String filterBy = filterOption.filterBy;
                String filterValue = filterOption.filterValue;
                if ("isEnabled".equals(filterBy) && ("1".equals(filterValue) || "0".equals(filterValue))) {
                    return companyRepository.findAllByIsEnabled("1".equals(filterValue), pageable).map(CompanyMapper::toDTO);
                } else if ("generalSearch".equals(filterBy) && filterValue != null) {
                    return companyRepository.findAllByNameContainingIgnoreCaseOrCompanyEmailContainingIgnoreCaseOrManagerEmailContainingIgnoreCaseOrRegistrationCodeContainingIgnoreCase(
                            filterValue, filterValue, filterValue, filterValue, pageable).map(CompanyMapper::toDTO);
                }
            }
        }

        return companyRepository.findAll(pageable).map(CompanyMapper::toDTO);
    }

    public List<CompanyDTO> findAllActiveMin() {
        List<Company> companies = companyRepository.findAllByIsEnabled(true);
        return companies.stream()
                .map(CompanyMapper::toDTO)
                .toList();
    }

    public ResponseEntity<Company> updateCompanyMin(Long id, CompanyDTO companyDTO) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(id));

        if (companyDTO.name() != null && !companyRepository.existsByName(companyDTO.name())) {
            company.setName(companyDTO.name());
        }
        if (companyDTO.companyEmail() != null && !companyRepository.existsByCompanyEmail(companyDTO.companyEmail())) {
            company.setCompanyEmail(companyDTO.companyEmail());
        }
        if (companyDTO.registrationCode() != null && !companyRepository.existsByRegistrationCode(companyDTO.registrationCode())) {
            company.setRegistrationCode(companyDTO.registrationCode());
        }

        Company updatedCompany = companyRepository.save(company);
        return ResponseEntity.ok(updatedCompany);
    }

    public ResponseEntity<Company> updateCompany(Long id, Company company) {
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(id));

        if (company.getName() != null && !companyRepository.existsByName(company.getName())) {
            existingCompany.setName(company.getName());
        }
        if (company.getCompanyEmail() != null && !companyRepository.existsByCompanyEmail(company.getCompanyEmail())) {
            existingCompany.setCompanyEmail(company.getCompanyEmail());
        }
        if (company.getRegistrationCode() != null && !companyRepository.existsByRegistrationCode(company.getRegistrationCode())) {
            existingCompany.setRegistrationCode(company.getRegistrationCode());
        }
        if (company.getManagerEmail() != null && !companyRepository.existsByManagerEmail(company.getManagerEmail())) {
            existingCompany.setManagerEmail(company.getManagerEmail());
        }

        Company updatedCompany = companyRepository.save(existingCompany);
        return ResponseEntity.ok(updatedCompany);
    }

    public ResponseEntity<?> addUserToCompanyByUuid(AddUserToCompanyRequestDTO userToCompanyRequestDTO) {
        Company company = companyRepository.findById(userToCompanyRequestDTO.companyId())
                .orElseThrow(() -> new CompanyNotFoundException(userToCompanyRequestDTO.companyId()));

        User user = userRepository.findByUuid(userToCompanyRequestDTO.userUniqId())
                .orElseThrow(() -> new UserNotFoundException(userToCompanyRequestDTO.userUniqId()));

        if (user.getCompany() != null) {
            return ResponseEntity.badRequest().body("User already belongs to a company");
        }

        user.setCompany(company);
        user.setRole(Role.EMPLOYEE);
        userRepository.save(user);
        company.addUser(user);
        Company savedCompany = companyRepository.save(company);

        return ResponseEntity.ok(companyModelAssembler.toModel(CompanyMapper.toDTO(savedCompany)));
    }
}
