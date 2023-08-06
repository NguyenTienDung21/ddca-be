package com.example.ddcabe.Stock;

import com.example.ddcabe.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface StockRepository extends JpaRepository<Stock, UUID> {
    void deleteByCreatedAtBefore(LocalDateTime createdAt);

    // Retrieves a Stock object based on the itemId
    Stock findByItemId(String itemId);

    // Retrieves a list of Stock objects based on the sessionId
    List<Stock> findBySessionId(UUID sessionId);

    // Retrieves a list of Stock objects filtering by operatorId and sessionId
    @Query("SELECT stk FROM Stock stk WHERE stk.session.id = :sessionId AND stk.user.id = :operatorId")
    List<Stock> findAssignedStocksBySessionIdAndOperatorId(@Param("sessionId") UUID sessionId, @Param("operatorId") UUID operatorId);

    // Updates the operator of the stocks with the given stockIds
    @Modifying
    @Query("UPDATE Stock s SET s.user = :operator WHERE s.id IN :stockIds")
    void updateStocksOperator(@Param("operator") User operator, @Param("stockIds") List<UUID> stockIds);

    // Retrieves a list of Stock objects based on the itemIds and sessionId
    List<Stock> findByItemIdInAndSessionIdAndLocationInAndQuantityIn(List<String> itemIds, UUID sessionId, List<String> locations, List<Integer> quantities);

    //Delete all stocks with the given session id
    int deleteBySessionId(UUID sessionId);

    // Retrieves a  Stock objects based on the itemId and lot and Quantity
    //stk.session.id = :sessionId AND stk.user.id = :operatorId
    @Query("SELECT stk FROM Stock stk WHERE stk.itemId = :itemId AND stk.lot = :lot AND stk.scan_quantity = :quantity AND stk.session.id = :sessionId")
    Optional<Stock> findByItemIdAndLotAndQuantityAndSession(String itemId, String lot, int quantity, UUID sessionId);

    // Updates the stock with the given scan_quantity and scan_lot using stock
    @Modifying
    @Query("UPDATE Stock s SET s.scan_quantity = :scan_quantity, s.scan_lot = :scan_lot WHERE s.itemId = :itemId AND s.lot = :lot AND s.quantity = :quantity")
    void updateStockScanQuantityAndScanLot(@Param("scan_quantity") int scan_quantity, @Param("scan_lot") int scan_lot, String itemId, String lot, int quantity);
}

