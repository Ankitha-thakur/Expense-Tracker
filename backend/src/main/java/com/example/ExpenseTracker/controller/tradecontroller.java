package com.example.ExpenseTracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.ExpenseTracker.entity.trade;
import com.example.ExpenseTracker.service.tradeservice;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trade")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://localhost:5176",
    "http://localhost:5177",
    "https://expense-tracker-7asux2o5i-ankitha-thakur.vercel.app"
})
public class tradecontroller {

    @Autowired
    private tradeservice tradeservice;

    // Save trade
    @PostMapping("/save")
    public trade savetrade(@RequestBody trade t) {
        return tradeservice.savetrade(t);
    }
    // Get all trades
    @GetMapping("/all")
    public List<trade> getalltrades() {
        return tradeservice.getalltrades();
    }

    // Get trade by ID
    @GetMapping("/findbyid/{id}")
    public Optional<trade> gettradebyid(@PathVariable Integer id) {
        return tradeservice.gettradebyid(id);
    }

    // Update trade
    @PutMapping("/update/{id}")
    public trade updatetrade(@PathVariable Integer id, @RequestBody trade t) {
        return tradeservice.updatetrade(id, t);
    }

    // Delete trade
    @DeleteMapping("/delete/{id}")
    public String deletetrade(@PathVariable Integer id) {
        tradeservice.deletetrade(id);
        return "Trade deleted successfully";
    }
    
    @GetMapping("/filterbydate/{date}")
    public List<trade> filter(@PathVariable String date) {
        return tradeservice.filter(date);
    }
    
    @GetMapping("/filterbycategory/{category}")
    public List<trade> filterbycategory(@PathVariable String category) {
        return tradeservice.filterbycategory(category);
    }
    
    @GetMapping("/filterbydaterange")
    public List<trade> filterByDateRange(@RequestParam String startDate,@RequestParam String endDate) {
        return tradeservice.filterByDateRange(startDate, endDate);
    }
    
    @GetMapping("/summary/category")
    public List<Object[]> getCategorySummary() {
        return tradeservice.getCategorySummary();
    }
}