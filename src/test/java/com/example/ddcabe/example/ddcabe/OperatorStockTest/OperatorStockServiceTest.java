package com.example.ddcabe.example.ddcabe.OperatorStockTest;

import com.example.ddcabe.OperatorStock.OperatorStockRepository;
import com.example.ddcabe.OperatorStock.OperatorStockService;
import com.example.ddcabe.Stock.Stock;
import com.example.ddcabe.Stock.StockRepository;
import com.example.ddcabe.Stock.StockService;
import com.example.ddcabe.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OperatorStockServiceTest {

    @Mock
    private OperatorStockRepository operatorStockRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockService stockService;

    @InjectMocks
    private OperatorStockService operatorStockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        operatorStockService = new OperatorStockService(operatorStockRepository, stockRepository,stockService);
    }

    @Test
    void addStocks_shouldSaveOperatorStocks() {

        // Arrange
        Optional<User> operator = Optional.of(new User());
        List<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock());
        stocks.add(new Stock());

        Exception exception = assertThrows(NullPointerException.class, () -> {
            // Act
            operatorStockService.addStocks(operator, stocks);

            // Assert
            verify(operatorStockRepository, times(2)).save(any());
        });
    }

    @Test
    void addStocks_shouldSkipExistingStocks() {
        // Arrange
        Optional<User> operator = Optional.of(new User());
        List<Stock> stocks = new ArrayList<>();
        Stock existingStock = new Stock();
        existingStock.setId(UUID.randomUUID());
        stocks.add(existingStock);

        when(operatorStockRepository.existsByOperatorAndStock(operator, Optional.of(existingStock))).thenReturn(true);

        Exception exception = assertThrows(NullPointerException.class, () -> {
            // Act
            operatorStockService.addStocks(operator, stocks);

            // Assert
            verify(operatorStockRepository, never()).save(any());
        });
    }

    @Test
    void updateStocks_shouldUpdateStocks() {
        // Arrange
        Stock stock = new Stock();
        stock.setItemId("123");
        stock.setLot("ABC");
        stock.setQuantity(10);
        UUID sessionId = UUID.randomUUID();

        Optional<Stock> persistedStock = Optional.of(stock);
        when(stockRepository.findByItemIdAndLotAndQuantityAndSession(stock.getItemId(), stock.getLot(), stock.getQuantity(), sessionId)).thenReturn(persistedStock);

        // Act
        operatorStockService.updateStocks(stock, sessionId);

        // Assert
        verify(stockRepository).updateStockScanQuantityAndScanLot(stock.getScan_quantity(), stock.getScan_lot(), stock.getItemId(), stock.getLot(), stock.getQuantity());
    }

}
