package com.artjom.qr_project.controller;

import com.artjom.qr_project.DTO.QrRequest;
import com.artjom.qr_project.service.QrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/Qr")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class QrController {
    private final QrService qrService;

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateQr(@RequestBody QrRequest qrRequest) {
        System.out.println("Generating QR code with request: " + qrRequest);
        return qrService.generateQr(qrRequest);
    }

    @GetMapping("/resolve/{uuid}")
    public ResponseEntity<String> resolveQr(@PathVariable String uuid) {
        return qrService.resolveQr(uuid);
    }

}
