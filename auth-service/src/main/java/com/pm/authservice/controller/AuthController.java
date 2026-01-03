package com.pm.authservice.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.pm.authservice.dto.LoginRequestDTO;
import com.pm.authservice.dto.LoginResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Optional;
import com.pm.authservice.service.AuthService;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @Operation(summary = "User login endpoint")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        // Call the authentication service to validate the user credentials
        Optional<String> tokenOptional = authService.authenticate(loginRequestDTO);

        if (tokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = tokenOptional.get();
        return ResponseEntity.ok(new LoginResponseDTO(token));

    }


    @Operation(summary = "Validate Token")
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String authHeader) {
        boolean isValid = authHeader == null || !authHeader.startsWith("Bearer");
        if (isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // Authorization: Bearer <token>
        return authService.validateToken(authHeader.substring(7))
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); 
    }
}
