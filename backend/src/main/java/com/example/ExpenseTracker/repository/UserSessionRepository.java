
package com.example.ExpenseTracker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ExpenseTracker.entity.UserSession;

@Repository
public interface UserSessionRepository
        extends JpaRepository<UserSession, Long> {

    Optional<UserSession> findByRefreshToken(String refreshToken);

    void deleteByRefreshToken(String refreshToken);
}
