package com.artjom.qr_project.DTO;

import com.artjom.qr_project.enums.PartnershipStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PartnershipDTO {
    private Long id;
    private LocalDateTime createdAt;

    private CompanyDTO initiator;
    private CompanyDTO partner;
    private List<CampaignDTO> campaigns;

    private PartnershipStatus status;
}
