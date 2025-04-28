package com.artjom.qr_project.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UUIDGeneratorService {
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
