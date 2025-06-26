package com.ifpb.turmalina.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private com.ifpb.turmalina.service.AuthService authService;

    @GetMapping("/auth-url")
    public ResponseEntity<String> getAuthUrl() {
        try {
            String authUrl = authService.getAuthUrl();
            return ResponseEntity.ok(authUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar URL de autenticação.");
        }
    }

    @GetMapping("/Callback")
    public ResponseEntity<String> saveCredentials(@RequestParam("code") String code) throws GeneralSecurityException, IOException {
            String accessToken = authService.saveCredentials(code);
            return ResponseEntity.ok(accessToken);

    }
}
