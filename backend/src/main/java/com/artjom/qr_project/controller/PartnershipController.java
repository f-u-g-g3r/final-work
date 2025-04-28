package com.artjom.qr_project.controller;

import com.artjom.qr_project.DTO.FilterRequest;
import com.artjom.qr_project.DTO.PartnershipActionDTO;
import com.artjom.qr_project.DTO.PartnershipDTO;
import com.artjom.qr_project.DTO.PartnershipRequestDTO;
import com.artjom.qr_project.DTO.mapper.PartnershipMapper;
import com.artjom.qr_project.assembler.PartnershipModelAssembler;
import com.artjom.qr_project.repository.PartnershipRepository;
import com.artjom.qr_project.service.PartnershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/Partnership")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PartnershipController {

    private final PartnershipService partnershipService;
    private final PartnershipRepository partnershipRepo;
    private final PartnershipModelAssembler assembler;

    @PostMapping("/request")
    public ResponseEntity<?> requestPartnership(@RequestBody PartnershipRequestDTO dto) {
        return partnershipService.requestPartnership(dto);
    }

    @PostMapping("/respond")
    public ResponseEntity<?> respondToRequest(@RequestBody PartnershipActionDTO dto) {
        return partnershipService.respondToRequest(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable Long id) {
        return partnershipService.one(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COMPANY_OWNER')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return partnershipService.delete(id);
    }

    @GetMapping
    public CollectionModel<EntityModel<PartnershipDTO>> all() {
        List<EntityModel<PartnershipDTO>> partnerships = partnershipRepo.findAll().stream()
                .map(PartnershipMapper::toDTO)
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(partnerships,
                linkTo(methodOn(PartnershipController.class).all()).withSelfRel());
    }

    @PostMapping("/getPartnershipRequests/{companyId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COMPANY_OWNER')")
    public Page<PartnershipDTO> getPartnershipRequests(@PathVariable Long companyId, @RequestBody FilterRequest filterRequest) {
        return partnershipService.getPartnershipRequests(companyId, filterRequest);
    }

    @PostMapping("/getPartnerships/{companyId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COMPANY_OWNER')")
    public Page<PartnershipDTO> getPartnerships(@PathVariable Long companyId, @RequestBody FilterRequest filterRequest) {
        return partnershipService.getPartnerships(companyId, filterRequest);
    }

    @PostMapping("/getInitiatedPartnerships/{companyId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COMPANY_OWNER')")
    public Page<PartnershipDTO> getInitiatedPartnerships(@PathVariable Long companyId, @RequestBody FilterRequest filterRequest) {
        return partnershipService.getInitiatedPartnerships(companyId, filterRequest);
    }
}