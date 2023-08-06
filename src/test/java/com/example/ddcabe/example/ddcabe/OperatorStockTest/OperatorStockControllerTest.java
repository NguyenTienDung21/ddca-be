package com.example.ddcabe.example.ddcabe.OperatorStockTest;

import com.example.ddcabe.HttpResponse.ResponseError;
import com.example.ddcabe.OperatorStock.OperatorStockController;
import com.example.ddcabe.OperatorStock.OperatorStockService;
import com.example.ddcabe.Stock.Stock;
import com.example.ddcabe.Stock.StockService;
import com.example.ddcabe.User.User;
import com.example.ddcabe.User.UserService;
import com.pusher.rest.Pusher;
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
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OperatorStockControllerTest {


    @Mock
    private OperatorStockService operatorStockService;

    @Mock
    private UserService userService;

    @Mock
    private StockService stockService;

    @Mock
    private Pusher pusher;

    @InjectMocks
    private OperatorStockController operatorStockController;

    private static final Logger LOGGER = Logger.getLogger(OperatorStockController.class.getName());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        operatorStockController = new OperatorStockController(operatorStockService, userService, stockService, pusher);
    }

    @Test
    void addStocks_shouldReturnOkResponse() {
        // Arrange
        String operatorUsername = "john";
        List<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock());
        stocks.add(new Stock());

        Optional<User> operatorOptional = Optional.of(new User());
        when(userService.findByUsername(operatorUsername)).thenReturn(operatorOptional);
        //when(stockService.updateStocks(stocks)).thenReturn(stocks);

        // Act
        ResponseEntity<?> response = operatorStockController.addStocks(operatorUsername, stocks);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(stocks, response.getBody());
        verify(operatorStockService).addStocks(operatorOptional, stocks);
        verify(pusher).trigger("stocks", "update", stocks);
    }


}
