
package com.example.ExpenseTracker.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ExpenseTracker.entity.AppAccount;
import com.example.ExpenseTracker.repository.AppAccountRepository;

@Service
public class AuthService {

    private final AppAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            AppAccountRepository accountRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AppAccount register(String username, String password) {

        if (accountRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        AppAccount account = new AppAccount(
            username,
            passwordEncoder.encode(password)
        );

        return accountRepository.save(account);
    }

    public AppAccount login(String username, String password) {

        AppAccount account = accountRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

       return account;
    }
}
