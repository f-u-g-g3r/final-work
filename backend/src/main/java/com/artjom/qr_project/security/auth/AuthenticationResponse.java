package com.artjom.qr_project.security.auth;

import com.artjom.qr_project.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private boolean isEmailTaken;

    @Enumerated(EnumType.STRING)
    private Role role;
    private String uuid;
    private Long id;

    public AuthenticationResponse(String token, String uuid, Role role, Long id) {
        this.token = token;
        this.uuid = uuid;
        this.role = role;
        this.id = id;
    }

    public AuthenticationResponse(String message) {
        if (message.equals("Email is taken") ) {
            this.isEmailTaken = true;
        }
    }
}
