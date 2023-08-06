package com.example.ddcabe.OperatorStock;

import com.example.ddcabe.Stock.Stock;
import com.example.ddcabe.Stock.StockRepository;
import com.example.ddcabe.Stock.StockService;
import com.example.ddcabe.User.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Service class for managing operator stock data.
 */
@Service
public class OperatorStockService {
    private final OperatorStockRepository operatorStockRepository;
    private final StockRepository stockRepository;

    private final Logger logger = Logger.getLogger(OperatorStockService.class.getName());

    /**
     * Constructs an OperatorStockService with the specified OperatorStockRepository.
     *
     * @param operatorStockRepository the repository for operator stock data
     */
    public OperatorStockService(OperatorStockRepository operatorStockRepository, StockRepository stockRepository, StockService stockService) {
        this.operatorStockRepository = operatorStockRepository;
        this.stockRepository = stockRepository;
    }

//    /**
//     * Adds a single stock to the operator's stock scans.
//     *
//     * @param operator The optional operator performing the stock scan.
//     * @param stock    The stock to be added.
//     */
//    public void addSingleStock(Optional<User> operator, Stock stock) {
//        // Create a new OperatorStockScan entity with the given operator, stock, and its quantity.
//        OperatorStock operatorStock = new OperatorStock(operator, stock, stock.getQuantity());
//
//        // Save the OperatorStockScan entity to the repository.
//        operatorStockRepository.save(operatorStock);
//    }

    /**
     * Adds multiple stocks to the operator's stock scans.
     *
     * @param operator The optional operator performing the stock scan.
     * @param stocks   The list of stocks to be added.
     */
    public void addStocks(Optional<User> operator, List<Stock> stocks) {
        logger.info("inside addStocks of OperatorStockService");
        logger.info("size of stock list: " + stocks.size());
        for (Stock stock : stocks) {
            logger.info("stocks: " + stock.toString());
            logger.info("stock belongs to session: " + stock.getSession());
            Optional<Stock> persistedStock = updateStocks(stock, stock.getSession().getId());
            System.out.println("132: " + stock.getScan_quantity());
            // Check if the stock already exists in the operators_stocks table
            if (operatorStockRepository.existsByOperatorAndStock(operator, persistedStock)) {
                continue; // Skip adding the stock if it already exists
            }
            // Create a new OperatorStock entity with the given operator, stock, and its quantity.
            OperatorStock operatorStock = new OperatorStock(operator, persistedStock, stock.getQuantity());
            // Save the OperatorStock entity to the repository.
            operatorStockRepository.save(operatorStock);
        }
    }

    //Find and Update Stock
    public Optional<Stock> updateStocks(Stock stock, UUID sessionId) {
        Optional<Stock> persistedStock = stockRepository.findByItemIdAndLotAndQuantityAndSession(stock.getItemId(), stock.getLot(), stock.getQuantity(), sessionId);
        persistedStock.ifPresent(value -> {
            value.setScan_quantity(stock.getScan_quantity());
            stockRepository.updateStockScanQuantityAndScanLot(stock.getScan_quantity(), stock.getScan_lot(), stock.getItemId(), stock.getLot(), stock.getQuantity());
        });
        return persistedStock;
    }
}

