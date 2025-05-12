package com.artjom.qr_project.DTO;

import com.artjom.qr_project.enums.PartnershipStatus;

import java.time.LocalDateTime;
import java.util.List;

public record PartnershipDTO(Long id, LocalDateTime createdAt, CompanyDTO initiator, CompanyDTO partner, List<CampaignDTO> campaigns, PartnershipStatus status) {
}
