
package com.example.ExpenseTracker.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ExpenseTracker.entity.AppAccount;
import com.example.ExpenseTracker.entity.UserSession;
import com.example.ExpenseTracker.repository.UserSessionRepository;

@Service
public class UserSessionService {

    private final UserSessionRepository sessionRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public UserSessionService(UserSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public String createSession(AppAccount account) {

        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);

        String token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);

        UserSession session = new UserSession();
        session.setAccount(account);
        session.setRefreshToken(token);
        session.setCreatedAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusDays(365));

        sessionRepository.save(session);

        return token;
    }

    public boolean isValid(String token) {
        return sessionRepository.findByRefreshToken(token)
                .filter(session -> session.getExpiresAt().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    @Transactional
    public void revokeSession(String token) {
        sessionRepository.deleteByRefreshToken(token);
    }
    public Optional<AppAccount> getAccount(String token) {
    return sessionRepository.findByRefreshToken(token)
            .filter(session ->
                session.getExpiresAt().isAfter(LocalDateTime.now())
            )
            .map(UserSession::getAccount);
}
}
