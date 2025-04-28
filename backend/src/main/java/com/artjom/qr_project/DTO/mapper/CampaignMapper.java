package com.artjom.qr_project.DTO.mapper;

import com.artjom.qr_project.DTO.CampaignDTO;
import com.artjom.qr_project.model.Campaign;


public class CampaignMapper {
    public static CampaignDTO toFlatDTO(Campaign campaign) {
        CampaignDTO dto = new CampaignDTO();
        dto.setId(campaign.getId());
        dto.setTitle(campaign.getTitle());
        dto.setDescription(campaign.getDescription());
        dto.setCreatedAt(campaign.getCreatedAt());
        dto.setValidUntil(campaign.getValidUntil());
        dto.setDiscountPercentage(campaign.getDiscountPercentage());
        dto.setInitiator(CompanyMapper.toDTO(campaign.getInitiator()));
        return dto;
    }

    public static CampaignDTO toDTO(Campaign campaign) {
        CampaignDTO dto = toFlatDTO(campaign);
        dto.setPartnerships(
                campaign.getPartnerships().stream()
                        .map(PartnershipMapper::toFlatDTO)
                        .toList()
        );
        return dto;
    }
}