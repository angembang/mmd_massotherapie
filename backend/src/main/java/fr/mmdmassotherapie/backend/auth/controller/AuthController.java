package fr.mmdmassotherapie.backend.auth.controller;

import fr.mmdmassotherapie.backend.auth.AuthService;
import fr.mmdmassotherapie.backend.auth.model.AuthResponse;
import fr.mmdmassotherapie.backend.auth.model.LoginRequest;
import fr.mmdmassotherapie.backend.auth.model.RegisterRequest;
import fr.mmdmassotherapie.backend.bo.LogicResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public LogicResult<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return new LogicResult<>(
                "ADMIN_REGISTERED",
                "Admin user registered",
                authService.register(request)
        );
    }

    @PostMapping("/login")
    public LogicResult<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return new LogicResult<>(
                "AUTHENTICATED",
                "Authentication successful",
                authService.login(request)
        );
    }
}
