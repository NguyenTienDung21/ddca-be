package com.example.ddcabe.example.ddcabe.StockTest;

import com.example.ddcabe.Session.Session;
import com.example.ddcabe.Stock.Stock;
import com.example.ddcabe.Stock.StockRepository;
import com.example.ddcabe.Stock.StockService;
import com.example.ddcabe.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StockServiceTest {


    @InjectMocks
    private StockService stockService;

    @Mock
    private StockRepository stockRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stockService = new StockService(stockRepository);
    }

    @Test
    void addStocks() {
        List<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock());
        stocks.add(new Stock());

        stockService.addStocks(stocks);

        verify(stockRepository, times(1)).saveAll(stocks);
    }



    @Test
    void getAssignedStocksBySessionIdAndOperatorId() {
        UUID sessionId = UUID.randomUUID();
        UUID operatorId = UUID.randomUUID();
        List<Stock> expectedStocks = new ArrayList<>();
        expectedStocks.add(new Stock());
        expectedStocks.add(new Stock());

        when(stockRepository.findAssignedStocksBySessionIdAndOperatorId(sessionId, operatorId)).thenReturn(expectedStocks);

        List<Stock> actualStocks = stockService.getAssignedStocksBySessionIdAndOperatorId(sessionId, operatorId);

        assertEquals(expectedStocks, actualStocks);
    }

    @Test
    void getStocksForSession() {
        UUID sessionId = UUID.randomUUID();
        List<Stock> expectedStocks = new ArrayList<>();
        expectedStocks.add(new Stock());
        expectedStocks.add(new Stock());

        when(stockRepository.findBySessionId(sessionId)).thenReturn(expectedStocks);

        List<Stock> actualStocks = stockService.getStocksForSession(sessionId);

        assertEquals(expectedStocks, actualStocks);
    }

    @Test
    void deleteStockByItemId_existingStock() {
        String itemId = "12345";
        Stock stock = new Stock();
        when(stockRepository.findByItemId(itemId)).thenReturn(stock);

        boolean result = stockService.deleteStockByItemId(itemId);

        assertTrue(result);
        verify(stockRepository, times(1)).delete(stock);
    }

    @Test
    void deleteStockByItemId_nonExistingStock() {
        String itemId = "12345";
        when(stockRepository.findByItemId(itemId)).thenReturn(null);

        boolean result = stockService.deleteStockByItemId(itemId);

        assertFalse(result);
        verify(stockRepository, never()).delete(any(Stock.class));
    }

    @Test
    void deleteStocksBySessionId() {
        UUID sessionId = UUID.randomUUID();
        int expectedDeletedCount = 5;

        when(stockRepository.deleteBySessionId(sessionId)).thenReturn(expectedDeletedCount);

        int actualDeletedCount = stockService.deleteStocksBySessionId(sessionId);

        assertEquals(expectedDeletedCount, actualDeletedCount);
    }




}
