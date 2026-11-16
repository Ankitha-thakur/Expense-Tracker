
package com.example.ExpenseTracker.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ExpenseTracker.entity.trade;
import com.example.ExpenseTracker.service.tradeservice;

@RestController
@RequestMapping("/trade")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://localhost:5176",
    "http://localhost:5177",
    "https://expense-tracker-beige-phi.vercel.app"
})
public class tradecontroller {

    @Autowired
    private tradeservice tradeservice;

    // Save expense
    @PostMapping("/save")
    public trade savetrade(
            @RequestBody trade t,
            Authentication authentication) {
        return tradeservice.savetrade(t, authentication.getName());
    }

    // Get all expenses belonging to logged-in user
    @GetMapping("/all")
    public List<trade> getalltrades(Authentication authentication) {
        return tradeservice.getalltrades(authentication.getName());
    }

    // Get expense by ID
    @GetMapping("/findbyid/{id}")
    public Optional<trade> gettradebyid(
            @PathVariable Integer id,
            Authentication authentication) {
        return tradeservice.gettradebyid(id, authentication.getName());
    }

    // Update expense
    @PutMapping("/update/{id}")
    public trade updatetrade(
            @PathVariable Integer id,
            @RequestBody trade t,
            Authentication authentication) {
        return tradeservice.updatetrade(id, t, authentication.getName());
    }

    // Delete expense
    @DeleteMapping("/delete/{id}")
    public String deletetrade(
            @PathVariable Integer id,
            Authentication authentication) {
        tradeservice.deletetrade(id, authentication.getName());
        return "Trade deleted successfully";
    }

    // Filter by date
    @GetMapping("/filterbydate/{date}")
    public List<trade> filter(
            @PathVariable String date,
            Authentication authentication) {
        return tradeservice.filter(date, authentication.getName());
    }

    // Filter by category
    @GetMapping("/filterbycategory/{category}")
    public List<trade> filterbycategory(
            @PathVariable String category,
            Authentication authentication) {
        return tradeservice.filterbycategory(
            category, authentication.getName()
        );
    }

    // Filter by date range
    @GetMapping("/filterbydaterange")
    public List<trade> filterByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate,
            Authentication authentication) {
        return tradeservice.filterByDateRange(
            startDate, endDate, authentication.getName()
        );
    }

    // Category summary
    @GetMapping("/summary/category")
    public List<Object[]> getCategorySummary(Authentication authentication) {
        return tradeservice.getCategorySummary(authentication.getName());
    }
}
