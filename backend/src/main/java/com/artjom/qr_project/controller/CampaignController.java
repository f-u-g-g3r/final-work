package com.artjom.qr_project.controller;

import com.artjom.qr_project.DTO.CampaignCreateDTO;
import com.artjom.qr_project.DTO.CampaignDTO;
import com.artjom.qr_project.DTO.FilterRequest;
import com.artjom.qr_project.DTO.mapper.CampaignMapper;
import com.artjom.qr_project.assembler.CampaignModelAssembler;
import com.artjom.qr_project.repository.CampaignRepository;
import com.artjom.qr_project.service.CampaignService;
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
@RequestMapping("/Campaign")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CampaignController {

    private final CampaignService campaignService;
    private final CampaignRepository campaignRepo;
    private final CampaignModelAssembler assembler;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CampaignCreateDTO dto) {
        return campaignService.createCampaign(dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CampaignCreateDTO dto) {
        return campaignService.updateCampaign(id, dto);
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return campaignService.deleteCampaign(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable Long id) {
        return campaignService.one(id);
    }

    @GetMapping
    public CollectionModel<EntityModel<CampaignDTO>> all() {
        List<EntityModel<CampaignDTO>> campaigns = campaignRepo.findAll().stream().map(CampaignMapper::toDTO)
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(campaigns,
                linkTo(methodOn(CampaignController.class).all()).withSelfRel());
    }

    @PostMapping("/initiatedByCompany/{companyId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COMPANY_OWNER')")
    public Page<CampaignDTO> getInitiatedByCompanyCampaigns(@PathVariable Long companyId, @RequestBody FilterRequest filterRequest) {
        return campaignService.getInitiatedByCompanyCampaigns(companyId, filterRequest);
    }

    @PostMapping("/beneficiaryOfCompany/{companyId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COMPANY_OWNER', 'EMPLOYEE')")
    public Page<CampaignDTO> getBeneficiaryOfCompanyCampaigns(@PathVariable Long companyId, @RequestBody FilterRequest filterRequest) {
        return campaignService.getBeneficiaryOfCompanyCampaigns(companyId, filterRequest);
    }
}