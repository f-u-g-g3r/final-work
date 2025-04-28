package com.artjom.qr_project.service;


import com.artjom.qr_project.DTO.FilterRequest;
import com.artjom.qr_project.DTO.PartnershipActionDTO;
import com.artjom.qr_project.DTO.PartnershipDTO;
import com.artjom.qr_project.DTO.PartnershipRequestDTO;
import com.artjom.qr_project.DTO.mapper.PartnershipMapper;
import com.artjom.qr_project.assembler.PartnershipModelAssembler;
import com.artjom.qr_project.controller.PartnershipController;
import com.artjom.qr_project.enums.PartnershipStatus;
import com.artjom.qr_project.model.Company;
import com.artjom.qr_project.model.Partnership;
import com.artjom.qr_project.repository.CompanyRepository;
import com.artjom.qr_project.repository.PartnershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class PartnershipService {

    private final CompanyRepository companyRepo;
    private final PartnershipRepository partnershipRepo;
    private final PartnershipModelAssembler partnershipAssembler;

    public ResponseEntity<?> requestPartnership(PartnershipRequestDTO dto) {
        Company initiator = companyRepo.findById(dto.getRequesterCompanyId())
                .orElseThrow(() -> new RuntimeException("Initiator not found"));
        Company partner = companyRepo.findById(dto.getTargetCompanyId())
                .orElseThrow(() -> new RuntimeException("Target company not found"));

        if (partnershipRepo.existsByPartnerIdAndInitiatorIdAndStatus(partner.getId(), initiator.getId(), PartnershipStatus.PENDING)
                || partnershipRepo.existsByPartnerIdAndInitiatorIdAndStatus(initiator.getId(), partner.getId(), PartnershipStatus.PENDING)) {
            return ResponseEntity.badRequest().body("Partnership request already exists.");
        } else if (partnershipRepo.existsByPartnerIdAndInitiatorIdAndStatus(initiator.getId(), partner.getId(), PartnershipStatus.ACCEPTED)
                || partnershipRepo.existsByPartnerIdAndInitiatorIdAndStatus(partner.getId(), initiator.getId(), PartnershipStatus.ACCEPTED)) {
            return ResponseEntity.badRequest().body("Partnership already exists.");
        }

        Partnership partnership = new Partnership();
        partnership.setInitiator(initiator);
        partnership.setPartner(partner);
        partnership.setCreatedAt(LocalDateTime.now());
        partnership.setStatus(PartnershipStatus.PENDING);

        Partnership saved = partnershipRepo.save(partnership);

        return ResponseEntity
                .created(linkTo(methodOn(PartnershipController.class).one(saved.getId())).toUri())
                .body(partnershipAssembler.toModel(PartnershipMapper.toDTO(saved)));
    }

    public ResponseEntity<?> respondToRequest(PartnershipActionDTO dto) {
        Partnership partnership = partnershipRepo.findById(dto.getPartnershipId())
                .orElseThrow(() -> new RuntimeException("Partnership not found"));

        if (partnership.getStatus() != PartnershipStatus.PENDING) {
            return ResponseEntity.badRequest().body("Request already processed.");
        }

        partnership.setStatus(dto.isAccepted() ? PartnershipStatus.ACCEPTED : PartnershipStatus.REJECTED);
        Partnership updated = partnershipRepo.save(partnership);

        return ResponseEntity.ok(partnershipAssembler.toModel(PartnershipMapper.toDTO(updated)));
    }

    public ResponseEntity<?> one(Long id) {
        Partnership partnership = partnershipRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        return ResponseEntity.ok(partnershipAssembler.toModel(PartnershipMapper.toDTO(partnership)));
    }

    public Page<PartnershipDTO> getPartnerships(Long companyId, FilterRequest filterRequest) {
        Pageable pageable = PageRequest.of(
                filterRequest.getPagingOptions().page,
                filterRequest.getPagingOptions().pageSize,
                filterRequest.getSortOptions().direction == 0 ? Sort.by(filterRequest.getSortOptions().sortBy).ascending()
                        : Sort.by(filterRequest.getSortOptions().sortBy).descending());

        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (filterRequest.getFilterOptions() != null) {
            for (FilterRequest.FilterOptions filterOption : filterRequest.getFilterOptions()) {
                String filterBy = filterOption.filterBy;
                String filterValue = filterOption.filterValue;

                if ("status".equals(filterBy)) {
                    PartnershipStatus status = PartnershipStatus.valueOf(filterValue.toUpperCase());
                    return partnershipRepo.findAllByPartnerAndStatusOrInitiatorAndStatus(company, status, company, status, pageable)
                            .map(PartnershipMapper::toDTO);
                } else {
                    throw new IllegalArgumentException("Invalid filter option: " + filterBy);
                }
            }
        }
        return partnershipRepo.findAllByPartner(
                company,
                pageable).map(PartnershipMapper::toDTO);
    }

    public Page<PartnershipDTO> getPartnershipRequests(Long companyId, FilterRequest filterRequest) {
        Pageable pageable = PageRequest.of(
                filterRequest.getPagingOptions().page,
                filterRequest.getPagingOptions().pageSize,
                filterRequest.getSortOptions().direction == 0 ? Sort.by(filterRequest.getSortOptions().sortBy).ascending()
                        : Sort.by(filterRequest.getSortOptions().sortBy).descending());

        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (filterRequest.getFilterOptions() != null) {
            for (FilterRequest.FilterOptions filterOption : filterRequest.getFilterOptions()) {
                String filterBy = filterOption.filterBy;
                String filterValue = filterOption.filterValue;

                if ("status".equals(filterBy)) {
                    PartnershipStatus status = PartnershipStatus.valueOf(filterValue.toUpperCase());
                    return partnershipRepo.findAllByPartnerAndStatus(company, status, pageable)
                            .map(PartnershipMapper::toDTO);
                } else {
                    throw new IllegalArgumentException("Invalid filter option: " + filterBy);
                }
            }
        }
        return partnershipRepo.findAllByPartner(
                company,
                pageable).map(PartnershipMapper::toDTO);
    }

    public Page<PartnershipDTO> getInitiatedPartnerships(Long companyId, FilterRequest filterRequest) {
        Pageable pageable = PageRequest.of(
                filterRequest.getPagingOptions().page,
                filterRequest.getPagingOptions().pageSize,
                filterRequest.getSortOptions().direction == 0 ? Sort.by(filterRequest.getSortOptions().sortBy).ascending()
                        : Sort.by(filterRequest.getSortOptions().sortBy).descending());

        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (filterRequest.getFilterOptions() != null) {
            for (FilterRequest.FilterOptions filterOption : filterRequest.getFilterOptions()) {
                String filterBy = filterOption.filterBy;
                String filterValue = filterOption.filterValue;

                if ("status".equals(filterBy)) {
                    PartnershipStatus status = PartnershipStatus.valueOf(filterValue.toUpperCase());
                    return partnershipRepo.findAllByInitiatorAndStatus(company, status, pageable)
                            .map(PartnershipMapper::toDTO);
                } else {
                    throw new IllegalArgumentException("Invalid filter option: " + filterBy);
                }
            }
        }
        return partnershipRepo.findAllByPartner(
                company,
                pageable).map(PartnershipMapper::toDTO);

    }

    public ResponseEntity<?> delete(Long id) {
        Partnership partnership = partnershipRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Partnership not found"));

        partnership.setStatus(PartnershipStatus.DELETED);
        Partnership updated = partnershipRepo.save(partnership);

        return ResponseEntity.ok(partnershipAssembler.toModel(PartnershipMapper.toDTO(updated)));
    }
}