package com.artjom.qr_project.security.auth.exceptions;

public class UsernameAlreadyTakenException extends RuntimeException {
    public UsernameAlreadyTakenException(String username) {
        super("Username '" + username + "' has been already taken.");
    }
}
