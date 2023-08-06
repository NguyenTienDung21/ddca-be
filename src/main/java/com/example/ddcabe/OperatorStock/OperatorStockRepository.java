package com.example.ddcabe.OperatorStock;

import com.example.ddcabe.Stock.Stock;
import com.example.ddcabe.User.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for accessing and manipulating OperatorStock entities in the database.
 */
@Repository
@Transactional
public interface OperatorStockRepository extends JpaRepository<OperatorStock, UUID> {
    // Delete operator stocks with createdAt before the specified date
    void deleteByCreatedAtBefore(LocalDateTime date);

    boolean existsByOperatorAndStock(Optional<User> operator, Optional<Stock> stock);
}


