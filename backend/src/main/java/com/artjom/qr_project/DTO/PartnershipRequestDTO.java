package com.artjom.qr_project.DTO;

import lombok.Data;

@Data
public class PartnershipRequestDTO {
    private Long requesterCompanyId;
    private Long targetCompanyId;
}
