package com.artjom.qr_project.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QrRequest {
    private Long campaignId;
    private Long ttlSeconds;
}
