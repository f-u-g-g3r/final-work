package com.artjom.qr_project.DTO.mapper;

import com.artjom.qr_project.DTO.CampaignDTO;
import com.artjom.qr_project.model.Campaign;


public class CampaignMapper {
    public static CampaignDTO toFlatDTO(Campaign campaign) {
        return new CampaignDTO(campaign.getId(),
                campaign.getTitle(),
                campaign.getDescription(),
                campaign.getDiscountPercentage(),
                campaign.getCreatedAt(),
                campaign.getValidUntil(),
                null,
                CompanyMapper.toDTO(campaign.getInitiator()));
    }

    public static CampaignDTO toDTO(Campaign campaign) {
        return new CampaignDTO(campaign.getId(),
                campaign.getTitle(),
                campaign.getDescription(),
                campaign.getDiscountPercentage(),
                campaign.getCreatedAt(),
                campaign.getValidUntil(),
                campaign.getPartnerships().stream()
                        .map(PartnershipMapper::toFlatDTO)
                        .toList(),
                CompanyMapper.toDTO(campaign.getInitiator()));
    }
}