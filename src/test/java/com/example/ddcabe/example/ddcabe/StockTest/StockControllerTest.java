package com.example.ddcabe.example.ddcabe.StockTest;

import com.example.ddcabe.Session.Session;
import com.example.ddcabe.Session.SessionService;
import com.example.ddcabe.Stock.Stock;
import com.example.ddcabe.Stock.StockController;
import com.example.ddcabe.Stock.StockService;
import com.example.ddcabe.User.User;
import com.example.ddcabe.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class StockControllerTest {

    @Mock
    private StockService stockService;
    @Mock
    private UserService userService;
    @Mock
    private SessionService sessionService;

    @InjectMocks
    private StockController stockController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stockController = new StockController(stockService, userService, sessionService);
    }

    @Test
    void testAddStocks() {
        String sessionName = "TestSession";
        List<Stock> stocks = new ArrayList<>();
        Session session = new Session();
        when(sessionService.getSessionByName(sessionName)).thenReturn(session);

        stockController.addStocks(sessionName, stocks);

        verify(sessionService, times(1)).getSessionByName(sessionName);
        verify(stockService, times(1)).addStocks(stocks);
    }

    @Test
    void testAssignStocksToOperator() {
        String operatorName = "TestOperator";
        String sessionName = "TestSession";
        List<Stock> stocks = new ArrayList<>();
        User operator = new User();
        Session session = new Session();
        when(userService.findByUsername(operatorName)).thenReturn(Optional.of(operator));
        when(sessionService.getSessionByName(sessionName)).thenReturn(session);

        stockController.assignStocksToOperator(operatorName, sessionName, stocks);

        verify(userService, times(1)).findByUsername(operatorName);
        verify(sessionService, times(1)).getSessionByName(sessionName);
        verify(stockService, times(1)).assignStocksToOperator(operator, session, stocks);
    }

    @Test
    void testGetStocksForSession() {
        String sessionName = "TestSession";
        Session session = new Session();
        when(sessionService.getSessionByName(sessionName)).thenReturn(session);
        when(stockService.getStocksForSession(session.getId())).thenReturn(new ArrayList<>());

        List<Stock> result = stockController.getStocksForSession(sessionName);

        verify(sessionService, times(1)).getSessionByName(sessionName);
        verify(stockService, times(1)).getStocksForSession(session.getId());
        assertEquals(0, result.size());
    }

    @Test
    void testGetStocksByOperator() {
        String operatorName = "TestOperator";
        String sessionName = "TestSession";
        User operator = new User();
        Session session = new Session();
        when(userService.findByUsername(operatorName)).thenReturn(Optional.of(operator));
        when(sessionService.getSessionByName(sessionName)).thenReturn(session);
        when(stockService.getAssignedStocksBySessionIdAndOperatorId(session.getId(), operator.getId())).thenReturn(new ArrayList<>());

        List<Stock> result = stockController.getStocksByOperator(sessionName, operatorName);

        verify(userService, times(1)).findByUsername(operatorName);
        verify(sessionService, times(1)).getSessionByName(sessionName);
        verify(stockService, times(1)).getAssignedStocksBySessionIdAndOperatorId(session.getId(), operator.getId());
        assertEquals(0, result.size());
    }

    @Test
    void testDeleteStock() {
        String itemId = "TestItemId";
        when(stockService.deleteStockByItemId(itemId)).thenReturn(true);

        ResponseEntity<?> response = stockController.deleteStock(itemId);

        verify(stockService, times(1)).deleteStockByItemId(itemId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteStocksBySessionId() {
        String sessionName = "TestSession";
        Session session = new Session();
        when(sessionService.getSessionByName(sessionName)).thenReturn(session);
        when(stockService.deleteStocksBySessionId(session.getId())).thenReturn(1);

        ResponseEntity<?> response = stockController.deleteStocksBySessionId(sessionName);

        verify(sessionService, times(1)).getSessionByName(sessionName);
        verify(stockService, times(1)).deleteStocksBySessionId(session.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
