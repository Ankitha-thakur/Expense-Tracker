package com.example.ExpenseTracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.ExpenseTracker.entity.trade;
import com.example.ExpenseTracker.repository.repository;




@Service
public class tradeservice {

    @Autowired
    private repository traderepo;

    // Save trade
    public trade savetrade(trade t) {
        return traderepo.save(t);
    }
    
    // Get all trades
    public List<trade> getalltrades() {
        return traderepo.findAll();
    }

    // Get trade by ID
    public Optional<trade> gettradebyid(Integer id) {
        return traderepo.findById(id);
    }

    // Update trade
    public trade updatetrade(Integer id, trade t) {
        trade update = traderepo.findById(id).orElseThrow(() -> new RuntimeException("Trade not found"));

        update.setTitle(t.getTitle());
        update.setAmount(t.getAmount());
        update.setCategory(t.getCategory());
        update.setDate(t.getDate());

        return traderepo.save(update);
    }

    // Delete trade
    public void deletetrade(Integer id) {
        traderepo.deleteById(id);
    }

    //filter by date
    public List<trade> filter(String date) {
        return traderepo.findByDate(date);
    }
    
 // Filter trades by category
    public List<trade> filterbycategory(String category) {
        return traderepo.findByCategoryIgnoreCase(category);
    }
    
    public List<trade> filterByDateRange(String startDate, String endDate) {
        return traderepo.findByDateBetween(startDate, endDate);
    }
    public List<Object[]> getCategorySummary() {
        return traderepo.getCategorySummary();
    }
    
}