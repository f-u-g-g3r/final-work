package com.artjom.qr_project.security.auth;

import com.artjom.qr_project.enums.Role;
import com.artjom.qr_project.model.Company;
import com.artjom.qr_project.model.User;
import com.artjom.qr_project.repository.CompanyRepository;
import com.artjom.qr_project.repository.UserRepository;
import com.artjom.qr_project.security.auth.exceptions.UsernameAlreadyTakenException;
import com.artjom.qr_project.security.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CompanyRepository companyRepository;

    public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {
        try {
            isUsernameNotTaken(registerRequest.getEmail());
        } catch (UsernameAlreadyTakenException e) {
            return ResponseEntity.badRequest().body(new AuthenticationResponse("Email is taken"));
        }

        User newUser = new User(registerRequest.getName(), registerRequest.getEmail(), registerRequest.getPassword(), Role.DISABLED_USER);
        newUser.setUuid(generateUUID());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        if (registerRequest.getCompanyId() != null && registerRequest.getCompanyId() != -1) {
            Company userCompany = companyRepository.findById(registerRequest.getCompanyId())
                    .orElseThrow(() -> new UsernameNotFoundException("Company not found"));
            newUser.setCompany(userCompany);
        }

        userRepository.save(newUser);

        HashMap extraClaims = new HashMap();
        extraClaims.put("id", newUser.getId());
        extraClaims.put("role", Role.DISABLED_USER);

        String jwtToken = jwtService.generateToken(extraClaims, newUser);

        return ResponseEntity.ok(new AuthenticationResponse(jwtToken, newUser.getUuid(), newUser.getRole(), newUser.getId()));
    }

    public ResponseEntity<?> login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        HashMap extraClaims = new HashMap();
        extraClaims.put("id", user.getId());
        extraClaims.put("role", user.getRole());

        String jwtToken = jwtService.generateToken(extraClaims, user);

        return ResponseEntity.ok(new AuthenticationResponse(jwtToken, user.getUuid(), user.getRole(), user.getId()));
    }

    private void isUsernameNotTaken(String username) {
        if (userRepository.existsByEmail(username) || companyRepository.existsByCompanyEmail(username)) {
            throw new UsernameAlreadyTakenException(username);
        }
    }

    public String generateUUID() {
        String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        StringBuilder uuid = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i<10; i++) {
            int randNum = rand.nextInt(0, 10);
            uuid.append(numbers[randNum]);
        }
        if (userRepository.findByUuid(uuid.toString()).equals(Optional.empty())) {
            return uuid.toString();
        } else {
            return generateUUID();
        }
    }
}
