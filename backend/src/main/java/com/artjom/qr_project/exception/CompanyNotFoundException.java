package com.artjom.qr_project.exception;

public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException(Long id) {
        super("Could not find company " + id);
    }
}
