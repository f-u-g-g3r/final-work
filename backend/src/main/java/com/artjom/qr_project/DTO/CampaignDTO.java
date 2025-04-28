package com.artjom.qr_project.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CampaignDTO {
    private Long id;
    private String title;
    private String description;
    private Integer discountPercentage;
    private LocalDateTime createdAt;
    private LocalDateTime validUntil;

    private List<PartnershipDTO> partnerships;

    private CompanyDTO initiator;
}
