
package com.example.ExpenseTracker.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ExpenseTracker.entity.AppAccount;
import com.example.ExpenseTracker.service.AuthService;
import com.example.ExpenseTracker.service.JwtService;
import com.example.ExpenseTracker.service.UserSessionService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://localhost:5174",
    "http://localhost:5176",
    "http://localhost:5177",
    "https://expense-tracker-beige-phi.vercel.app"
})
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserSessionService sessionService;

    public AuthController(
            AuthService authService,
            JwtService jwtService,
            UserSessionService sessionService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.sessionService = sessionService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AppAccount account) {
        try {
            authService.register(
                account.getUsername(),
                account.getPassword()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                .body("Account registered successfully");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AppAccount account) {
        try {
            AppAccount loggedInAccount = authService.login(
                account.getUsername(),
                account.getPassword()
            );

            String accessToken =
                jwtService.generateToken(loggedInAccount.getUsername());

            String refreshToken =
                sessionService.createSession(loggedInAccount);

            return ResponseEntity.ok(Map.of(
                "token", accessToken,
                "refreshToken", refreshToken
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {

        String refreshToken = body.get("refreshToken");

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return sessionService.getAccount(refreshToken)
            .map(account -> ResponseEntity.ok(Map.of(
                "token", jwtService.generateToken(account.getUsername())
            )))
            .orElseGet(() ->
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> body) {

        String refreshToken = body.get("refreshToken");

        if (refreshToken != null) {
            sessionService.revokeSession(refreshToken);
        }

        return ResponseEntity.ok("Logged out successfully");
    }
}
