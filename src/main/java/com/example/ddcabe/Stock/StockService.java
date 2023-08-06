package com.example.ddcabe.Stock;

import com.example.ddcabe.Session.Session;
import com.example.ddcabe.User.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    /**
     * Adds a list of stocks to the stock repository.
     *
     * @param stocks The list of stocks to be added.
     */
    public void addStocks(List<Stock> stocks) {
        stockRepository.saveAll(stocks);
    }

    /**
     * Updates a list of stocks in the stock repository.
     *
     * @param stocks The list of stocks to be updated.
     */
    public void assignStocksToOperator(User operator, Session session, List<Stock> stocks) {
        List<Stock> existingStocks = getExistingStocks(stocks, session);
        System.out.println("Existing stocks: " + existingStocks.size());
        for (Stock stock : existingStocks) {
            System.out.println("Stock ID: " + stock.getId());
            if (stock.getUser() != null) {
                System.out.println("Stock user: " + stock.getUser().getUsername());
                stock.setUser(null);
            }
        }
        List<UUID> existingStockIds = existingStocks.stream().map(Stock::getId).collect(Collectors.toList());
        stockRepository.updateStocksOperator(operator, existingStockIds);
    }

    /**
     * Retrieves a list of stocks associated with the specified operator ID and session ID.
     *
     * @param stocks The list of stocks to be updated.
     */
    private List<Stock> getExistingStocks(List<Stock> stocks, Session session) {
        List<String> itemIds = stocks.stream().map(Stock::getItemId).collect(Collectors.toList());
        List<String> locations = stocks.stream().map(Stock::getLocation).collect(Collectors.toList());
        List<Integer> quantities = stocks.stream().map(Stock::getQuantity).collect(Collectors.toList());

        return stockRepository.findByItemIdInAndSessionIdAndLocationInAndQuantityIn(
                itemIds,
                session.getId(),
                locations,
                quantities);
    }

    /**
     * Retrieves a list of stocks associated with the specified operator ID.
     *
     * @param sessionId  The ID of the session.
     * @param operatorId The ID of the operator.
     * @return A list of stocks associated with the specified operator ID and session ID.
     */
    public List<Stock> getAssignedStocksBySessionIdAndOperatorId(UUID sessionId, UUID operatorId) {
        return stockRepository.findAssignedStocksBySessionIdAndOperatorId(sessionId, operatorId);
    }

    /**
     * Retrieves stocks associated with a specific session ID from the stock repository.
     *
     * @param sessionId the ID of the session
     * @return a list of Stock objects
     */
    public List<Stock> getStocksForSession(UUID sessionId) {
        return stockRepository.findBySessionId(sessionId);
    }

    /**
     * Deletes a stock with the specified item ID.
     *
     * @param itemId The ID of the stock item to be deleted.
     * @return true if the stock was deleted, false if it does not exist.
     */
    public boolean deleteStockByItemId(String itemId) {
        Stock stock = stockRepository.findByItemId(itemId);
        if (stock != null) {
            stockRepository.delete(stock);
            return true;
        }
        return false;
    }

    /**
     * Deletes all stocks associated with a specific session ID from the stock repository.
     *
     * @param sessionId the ID of the session
     */
    public int deleteStocksBySessionId(UUID sessionId) {
        return stockRepository.deleteBySessionId(sessionId);
    }

    /**
     * Updates the scan_quantity and scan_lot of a list of stocks in the stock repository.
     *
     * @param listToUpdate The list of stocks to be updated.
     */

    public void updateStocks(List<Stock> listToUpdate) {
        //Loop through the stock, and update each item
        for (Stock operatorSentStock : listToUpdate) {
            log.info("Updating stock: " + operatorSentStock.getItemId() + " with quantity: " + operatorSentStock.getScan_quantity() + " and lot: " + operatorSentStock.getScan_lot());
            this.stockRepository.updateStockScanQuantityAndScanLot(operatorSentStock.getScan_quantity(), operatorSentStock.getScan_lot(), operatorSentStock.getItemId(), operatorSentStock.getLot(), operatorSentStock.getQuantity());
        }
    }

}
