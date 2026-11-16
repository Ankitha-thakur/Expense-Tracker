
package com.example.ExpenseTracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ExpenseTracker.entity.trade;
import com.example.ExpenseTracker.entity.AppAccount;
import com.example.ExpenseTracker.repository.repository;
import com.example.ExpenseTracker.repository.AppAccountRepository;

@Service
public class tradeservice {

    @Autowired
    private repository traderepo;

    @Autowired
    private AppAccountRepository accountRepository;

    // Save expense for a particular account
    public trade savetrade(trade t, String username) {
        AppAccount account = accountRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Account not found"));

        t.setAccount(account);
        return traderepo.save(t);
    }

    // Get expenses belonging to a particular account
    public List<trade> getalltrades(String username) {
        return traderepo.findByAccount_Username(username);
    }

    // Find expense by ID and account
    public Optional<trade> gettradebyid(Integer id, String username) {
        return traderepo.findByIdAndAccount_Username(id, username);
    }

    // Update expense belonging to a particular account
    public trade updatetrade(Integer id, trade t, String username) {
        trade update = traderepo.findByIdAndAccount_Username(id, username)
            .orElseThrow(() -> new RuntimeException("Trade not found"));

        update.setTitle(t.getTitle());
        update.setAmount(t.getAmount());
        update.setCategory(t.getCategory());
        update.setDate(t.getDate());

        return traderepo.save(update);
    }

    // Delete expense belonging to a particular account
    public void deletetrade(Integer id, String username) {
        trade existing = traderepo.findByIdAndAccount_Username(id, username)
            .orElseThrow(() -> new RuntimeException("Trade not found"));

        traderepo.delete(existing);
    }

    // Filter by date
    public List<trade> filter(String date, String username) {
        return traderepo.findByAccount_UsernameAndDate(username, date);
    }

    // Filter by category
    public List<trade> filterbycategory(String category, String username) {
        return traderepo.findByAccount_UsernameAndCategoryIgnoreCase(
            username, category
        );
    }

    // Filter by date range
    public List<trade> filterByDateRange(
            String startDate, String endDate, String username) {
        return traderepo.findByAccount_UsernameAndDateBetween(
            username, startDate, endDate
        );
    }

    // Category summary will be account-specific
    public List<Object[]> getCategorySummary(String username) {
        return traderepo.getCategorySummaryByUsername(username);
    }
}
