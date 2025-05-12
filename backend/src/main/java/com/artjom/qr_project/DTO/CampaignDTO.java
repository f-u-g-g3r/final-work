package com.artjom.qr_project.DTO;


import java.time.LocalDateTime;
import java.util.List;

public record CampaignDTO(Long id,
                          String title,
                          String description,
                          Integer discountPercentage,
                          LocalDateTime createdAt,
                          LocalDateTime validUntil,
                          List<PartnershipDTO> partnerships,
                          CompanyDTO initiator) {
}
