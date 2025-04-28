package com.artjom.qr_project.controller;

import com.artjom.qr_project.DTO.AddUserToCompanyRequestDTO;
import com.artjom.qr_project.DTO.CompanyDTO;
import com.artjom.qr_project.DTO.FilterRequest;
import com.artjom.qr_project.model.Company;
import com.artjom.qr_project.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/Company")
@RestController
@CrossOrigin(origins = "*")
public class CompanyController {
    private final CompanyService service;

    @GetMapping
    public CollectionModel<EntityModel<CompanyDTO>> all() {
        return service.findAll();
    }

    @GetMapping("/getActiveCompaniesMin")
    public List<CompanyDTO> getCompaniesMin() {
        return service.findAllActiveMin();
    }

    @PostMapping("getCompanies")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<CompanyDTO> getCompanies(@RequestBody FilterRequest filterRequest) {
        return service.findWithFilter(filterRequest);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public EntityModel<CompanyDTO> one(@PathVariable Long id) {
        return service.findById(id);
    }

    @PatchMapping("/updateCompanyMin/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COMPANY_OWNER')")
    public ResponseEntity<Company> updateCompanyMin(@PathVariable Long id, @RequestBody CompanyDTO companyDTO) {
        return service.updateCompanyMin(id, companyDTO);
    }

    @PatchMapping("/updateCompany/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company company) {
        return service.updateCompany(id, company);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DISABLED_USER')")
    public ResponseEntity<?> add(@Valid @RequestBody CompanyDTO company) {
        return service.add(company);
    }

    @PatchMapping("/activate/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> activate(@PathVariable Long id) {
        return service.activateCompany(id);
    }

    @PatchMapping("/deactivate/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deactivate(@PathVariable Long id) {
        return service.deactivateCompany(id);
    }

    @PostMapping("/addUserToCompanyByUuid")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COMPANY_OWNER')")
    public ResponseEntity<?> addUserToCompanyByUuid(@RequestBody AddUserToCompanyRequestDTO userToCompanyRequestDTO) {
        return service.addUserToCompanyByUuid(userToCompanyRequestDTO);
    }
}
