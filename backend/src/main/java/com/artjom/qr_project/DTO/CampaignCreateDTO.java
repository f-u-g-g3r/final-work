package com.artjom.qr_project.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CampaignCreateDTO {
    private List<Long> partnershipsIds;
    private Long initiatorId;

    private String title;
    private String description;
    private Integer discountPercentage;

    private LocalDateTime validUntil;

    public CampaignCreateDTO(List<Long> partnershipsIds, Long initiatorId, String title, String description, Integer discountPercentage) {
        this.partnershipsIds = partnershipsIds;
        this.initiatorId = initiatorId;
        this.title = title;
        this.description = description;
        this.discountPercentage = discountPercentage;
    }
}
