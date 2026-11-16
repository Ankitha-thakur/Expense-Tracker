
package com.example.ExpenseTracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ExpenseTracker.entity.trade;
@Repository
public interface repository extends JpaRepository<trade, Integer> {

    List<trade> findByDate(String date);

    List<trade> findByCategoryIgnoreCase(String category);

    List<trade> findByDateBetween(String startDate, String endDate);

    List<trade> findByAccount_Username(String username);

    Optional<trade> findByIdAndAccount_Username(Integer id, String username);

    List<trade> findByAccount_UsernameAndDate(String username, String date);

    List<trade> findByAccount_UsernameAndCategoryIgnoreCase(
        String username, String category
    );

    List<trade> findByAccount_UsernameAndDateBetween(
        String username, String startDate, String endDate
    );

 @Query("SELECT t.category, SUM(t.amount) FROM trade t WHERE t.account.username = :username GROUP BY t.category")
List<Object[]> getCategorySummaryByUsername(@Param("username") String username);
}
