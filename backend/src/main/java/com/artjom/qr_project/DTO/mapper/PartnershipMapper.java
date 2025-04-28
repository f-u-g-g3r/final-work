package com.artjom.qr_project.DTO.mapper;

import com.artjom.qr_project.DTO.PartnershipDTO;
import com.artjom.qr_project.model.Partnership;

import java.util.stream.Collectors;

public class PartnershipMapper {
    public static PartnershipDTO toFlatDTO(Partnership partnership) {
        PartnershipDTO dto = new PartnershipDTO();
        dto.setId(partnership.getId());
        dto.setCreatedAt(partnership.getCreatedAt());
        dto.setInitiator(CompanyMapper.toDTO(partnership.getInitiator()));
        dto.setPartner(CompanyMapper.toDTO(partnership.getPartner()));
        dto.setStatus(partnership.getStatus());
        return dto;
    }

    public static PartnershipDTO toDTO(Partnership partnership) {
        PartnershipDTO dto = toFlatDTO(partnership);
        dto.setCampaigns(
                partnership.getCampaigns().stream()
                        .map(CampaignMapper::toFlatDTO)
                        .collect(Collectors.toList())
        );
        return dto;
    }
}
