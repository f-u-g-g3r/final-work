package com.artjom.qr_project.DTO.mapper;

import com.artjom.qr_project.DTO.PartnershipDTO;
import com.artjom.qr_project.model.Partnership;

import java.util.stream.Collectors;

public class PartnershipMapper {
    public static PartnershipDTO toFlatDTO(Partnership partnership) {
        return new PartnershipDTO(
                partnership.getId(),
                partnership.getCreatedAt(),
                CompanyMapper.toDTO(partnership.getInitiator()),
                CompanyMapper.toDTO(partnership.getPartner()),
                null,
                partnership.getStatus()
        );
    }

    public static PartnershipDTO toDTO(Partnership partnership) {
        return new PartnershipDTO(
                partnership.getId(),
                partnership.getCreatedAt(),
                CompanyMapper.toDTO(partnership.getInitiator()),
                CompanyMapper.toDTO(partnership.getPartner()),
                partnership.getCampaigns().stream()
                        .map(CampaignMapper::toFlatDTO)
                        .collect(Collectors.toList()),
                partnership.getStatus()
        );
    }
}
