package com.example.ddcabe.Stock;

import com.example.ddcabe.Session.Session;
import com.example.ddcabe.Session.SessionService;
import com.example.ddcabe.User.User;
import com.example.ddcabe.User.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/stocks")
@CrossOrigin(origins = "*")
@Slf4j
public class StockController {
    private final StockService stockService;
    private final UserService userService;
    private final SessionService sessionService;

    private static final Logger logger = Logger.getLogger(StockController.class.getName());

    public StockController(StockService stockService, UserService userService, SessionService sessionService) {
        this.stockService = stockService;
        this.userService = userService;
        this.sessionService = sessionService;
    }

    /**
     * Endpoint to add a list of stocks to the database.
     *
     * @param sessionName The name of the session.
     * @param stocks      The list of stocks to be added.
     */
    @PostMapping("/add")
    public void addStocks(@RequestParam String sessionName, @RequestBody List<Stock> stocks) {
        // Decode the session name
        String decodeSessionName = URLDecoder.decode(sessionName, StandardCharsets.UTF_8);
        // Get the session by name
        Session session = sessionService.getSessionByName(decodeSessionName);
        // Set the session for each stock
        stocks.forEach(stock -> stock.setSession(session));
        // Add the stocks to the database
        stockService.addStocks(stocks);
    }

    @PostMapping("/assign")
    public void assignStocksToOperator(@RequestParam String operatorName, @RequestParam String sessionName, @RequestBody List<Stock> stocks) {
        // Find the operator name
        User operator = userService.findByUsername(operatorName).orElse(null);
        // Decode the session name
        String decodeSessionName = URLDecoder.decode(sessionName, StandardCharsets.UTF_8);
        // Get the session by name
        Session session = sessionService.getSessionByName(decodeSessionName);
        for (Stock stock : stocks) {
            System.out.println("Stock ID: " + stock);
        }
        stockService.assignStocksToOperator(operator, session, stocks);
    }

    /**
     * Get stocks for a specific session.
     *
     * @param sessionName the name of the session
     * @return a list of stocks for the specified session
     */
    @GetMapping("/admin")
    public List<Stock> getStocksForSession(@RequestParam String sessionName) {
        // Decode the session name
        String decodeSessionName = URLDecoder.decode(sessionName, StandardCharsets.UTF_8);
        // Get the session by name
        Session session = sessionService.getSessionByName(decodeSessionName);
        // Return the stocks for the session
        return stockService.getStocksForSession(session.getId());
    }

    /**
     * Endpoint to get a list of stocks based on the operator name.
     *
     * @param operatorName The name of the operator.
     * @return The list of stocks associated with the operator.
     */
    @GetMapping("/operator")
    public List<Stock> getStocksByOperator(@RequestParam String sessionName, @RequestParam String operatorName) {
        logger.info("inside /operator getStocksByOperator");
        Optional<User> operator = userService.findByUsername(operatorName);
        String decodeSessionName = URLDecoder.decode(sessionName, StandardCharsets.UTF_8);
        Session session = sessionService.getSessionByName(decodeSessionName);
        if (operator.isPresent()) {
            List<Stock> stocks = stockService.getAssignedStocksBySessionIdAndOperatorId(session.getId(), operator.get().getId());
            System.out.println("Stock size: " + stocks.size());
            for (Stock stock :
                    stocks) {
                logger.info("session uuid: " +stock.getSession().getId().toString());
            }
            return stocks;
        }
        return null;
    }

    /**
     * Endpoint to delete a stock by its ID.
     *
     * @param itemId The ID of the stock to be deleted.
     * @return ResponseEntity indicating the status of the deletion.
     */
    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deleteStock(@PathVariable String itemId) {
        boolean deleted = stockService.deleteStockByItemId(itemId);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Endpoint to delete all stocks for a specific session.
     *
     * @param sessionName The name of the session.
     * @return ResponseEntity indicating the status of the deletion.
     */
    @DeleteMapping("/delete-stocks-session")
    public ResponseEntity<?> deleteStocksBySessionId(@RequestParam String sessionName) {
        String decodeSessionName = URLDecoder.decode(sessionName, StandardCharsets.UTF_8);
        Session session = sessionService.getSessionByName(decodeSessionName);
        int deleted = stockService.deleteStocksBySessionId(session.getId());
        System.out.println("Deleted: " + deleted);
        if (deleted != 0) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

//    @PutMapping("/update")
//    public ResponseEntity<?> updateStocks(@RequestParam String operatorUsername, @RequestBody List<Stock> stocks) {
//        Optional<User> operatorOptional = userService.findByUsername(operatorUsername);
//        if (operatorOptional.isPresent()) {
//            User operator = operatorOptional.get();
//            int maxStockUpdatesPerRequest = 100;
//            for (Stock stock : stocks) {
//                socketIOController.sendStockUpdate(stock);
//            }
//            if (stocks.size() > maxStockUpdatesPerRequest) {
//                for (Stock stock : stocks) {
//                    stockUpdateKafkaProducer.sendStockUpdate(operator, stock);
//                }
//            } else {
//                stockService.updateStocks(operator.getId(), stocks);
//                for (Stock stock : stocks) {
//                    socketIOController.sendStockUpdate(stock);
//                }
//            }
//            return ResponseEntity.ok().build();
//        } else {
//            ResponseError errorResponse = new ResponseError("Operator not found", 404);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
//        }
//    }
