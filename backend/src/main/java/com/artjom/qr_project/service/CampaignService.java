package com.artjom.qr_project.service;

import com.artjom.qr_project.DTO.CampaignCreateDTO;
import com.artjom.qr_project.DTO.CampaignDTO;
import com.artjom.qr_project.DTO.FilterRequest;
import com.artjom.qr_project.DTO.mapper.CampaignMapper;
import com.artjom.qr_project.assembler.CampaignModelAssembler;
import com.artjom.qr_project.controller.CampaignController;
import com.artjom.qr_project.model.Campaign;
import com.artjom.qr_project.model.Company;
import com.artjom.qr_project.model.Partnership;
import com.artjom.qr_project.repository.CampaignRepository;
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
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepo;
    private final CompanyRepository companyRepo;
    private final PartnershipRepository partnershipRepo;
    private final CampaignModelAssembler campaignAssembler;

    public Optional<Campaign> findById(Long id) {
        return campaignRepo.findById(id);
    }

    public ResponseEntity<?> createCampaign(CampaignCreateDTO dto) {
        List<Partnership> partnerships = partnershipRepo.findAllById(dto.getPartnershipsIds());

        Company initiator = companyRepo.findById(dto.getInitiatorId())
                .orElseThrow(() -> new RuntimeException("Initiator not found"));

        Campaign campaign = new Campaign();
        campaign.setTitle(dto.getTitle());
        campaign.setPartnerships(partnerships);
        campaign.setInitiator(initiator);
        campaign.setCreatedAt(LocalDateTime.now());
        campaign.setValidUntil(dto.getValidUntil());
        campaign.setDescription(dto.getDescription());
        campaign.setDiscountPercentage(dto.getDiscountPercentage());

        Campaign saved = campaignRepo.save(campaign);

        return ResponseEntity
                .created(linkTo(methodOn(CampaignController.class).one(saved.getId())).toUri())
                .body(campaignAssembler.toModel(CampaignMapper.toDTO(saved)));
    }

    public ResponseEntity<?> updateCampaign(Long id, CampaignCreateDTO dto) {
        Campaign campaign = campaignRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        campaign.setDescription(dto.getDescription());
        campaign.setDiscountPercentage(dto.getDiscountPercentage());
        campaign.setValidUntil(dto.getValidUntil());

        Campaign updated = campaignRepo.save(campaign);

        return ResponseEntity.ok(campaignAssembler.toModel(CampaignMapper.toDTO(updated)));
    }

    public ResponseEntity<?> disableCampaign(Long id) {
        Campaign campaign = campaignRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        campaign.setValidUntil(LocalDateTime.now()); // или campaign.setActive(false)
        Campaign updated = campaignRepo.save(campaign);

        return ResponseEntity.ok(campaignAssembler.toModel(CampaignMapper.toDTO(updated)));
    }

    public ResponseEntity<?> one(Long id) {
        Campaign campaign = campaignRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        return ResponseEntity.ok(campaignAssembler.toModel(CampaignMapper.toDTO(campaign)));
    }

    public Page<CampaignDTO> getInitiatedByCompanyCampaigns(Long companyId, FilterRequest filterRequest) {
        Pageable pageable = PageRequest.of(
                filterRequest.getPagingOptions().page,
                filterRequest.getPagingOptions().pageSize,
                filterRequest.getSortOptions().direction == 0 ? Sort.by(filterRequest.getSortOptions().sortBy).ascending()
                        : Sort.by(filterRequest.getSortOptions().sortBy).descending());

        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        return campaignRepo.findAllByInitiator(
                company,
                pageable).map(CampaignMapper::toDTO);
    }

    public Page<CampaignDTO> getBeneficiaryOfCompanyCampaigns(Long companyId, FilterRequest filterRequest) {
        Pageable pageable = PageRequest.of(
                filterRequest.getPagingOptions().page,
                filterRequest.getPagingOptions().pageSize,
                filterRequest.getSortOptions().direction == 0 ? Sort.by(filterRequest.getSortOptions().sortBy).ascending()
                        : Sort.by(filterRequest.getSortOptions().sortBy).descending());


        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        List<Partnership> partnerships = partnershipRepo.findAllByPartnerOrInitiator(company, company);
        return campaignRepo.findAllByPartnershipsInAndInitiatorNot(
                partnerships,
                company,
                pageable).map(CampaignMapper::toDTO);
    }
}