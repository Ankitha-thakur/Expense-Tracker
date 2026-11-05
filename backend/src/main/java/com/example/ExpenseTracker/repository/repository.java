
package com.example.ExpenseTracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.ExpenseTracker.entity.trade;

@Repository
public interface repository extends JpaRepository<trade, Integer> {

    List<trade> findByDate(String date);

    List<trade> findByCategoryIgnoreCase(String category);

    List<trade> findByDateBetween(String startDate, String endDate);

    @Query("SELECT t.category, SUM(t.amount) FROM trade t GROUP BY t.category")
    List<Object[]> getCategorySummary();

}
