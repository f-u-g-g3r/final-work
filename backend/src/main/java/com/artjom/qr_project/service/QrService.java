package com.artjom.qr_project.service;

import com.artjom.qr_project.DTO.QrRequest;
import com.artjom.qr_project.model.Campaign;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class QrService {
    private final RedisTemplate<String, String> redisTemplate;
    private final UUIDGeneratorService uuidGeneratorService;
    private final CampaignService campaignService;

    @Value("${frontend.url}")
    private String frontendUrl;


    public ResponseEntity<Map<String, String>> generateQr(QrRequest qrRequest) {
        try {
            if (qrRequest.getCampaignId() == null || qrRequest.getTtlSeconds() == null) {
                return ResponseEntity.badRequest().build();
            }

            Campaign campaign = campaignService.findById(qrRequest.getCampaignId())
                    .orElseThrow(() -> new RuntimeException("Campaign not found"));

            Map<String, Object> qrData = new HashMap<>();
            qrData.put("campaignId", campaign.getId());
            qrData.put("title", campaign.getTitle());
            qrData.put("description", campaign.getDescription());
            qrData.put("discountPercentage", campaign.getDiscountPercentage());
            qrData.put("validUntil", campaign.getValidUntil() != null ? campaign.getValidUntil().toString() : null);
            qrData.put("createdAt", Instant.now().toString());

            String uuid = uuidGeneratorService.generate();
            ObjectMapper mapper = new ObjectMapper();
            String qrDataJson = mapper.writeValueAsString(qrData);

            redisTemplate.opsForValue().set("qr:" + uuid, qrDataJson, qrRequest.getTtlSeconds(), TimeUnit.SECONDS);

            Map<String, String> response = new HashMap<>();
            response.put("qrUrl", frontendUrl + "/qr/scan/" + uuid);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("Error generating QR code: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("message", "Internal server error"));
        }
    }


    public ResponseEntity<String> resolveQr(String uuid) {
        try {
            String redisKey = "qr:" + uuid;

            String data = redisTemplate.opsForValue().get(redisKey);

            if (data == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"QR expired, invalid or already used\"}");
            }

            redisTemplate.delete(redisKey);

            return ResponseEntity.ok(data);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\":\"Internal server error\"}");
        }
    }
}
