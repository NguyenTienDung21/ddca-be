package com.example.ddcabe.OperatorStock;

import com.example.ddcabe.HttpResponse.ResponseError;
import com.example.ddcabe.Stock.Stock;
import com.example.ddcabe.Stock.StockService;
import com.example.ddcabe.User.User;
import com.example.ddcabe.User.UserService;
import com.pusher.rest.Pusher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/assign")
@CrossOrigin(origins = "*")
public class OperatorStockController {
    private final OperatorStockService operatorStockService;

    private final StockService stockService;
    private final UserService userService;
    //    private final Producer stockUpdateKafkaProducer;
    private final Pusher pusher;

    private static final Logger LOGGER = Logger.getLogger(OperatorStockController.class.getName());


    public OperatorStockController(OperatorStockService operatorStockService, UserService userService, StockService stockService, Pusher pusher) {
        this.operatorStockService = operatorStockService;
        this.userService = userService;
        this.stockService = stockService;
        this.pusher = pusher;
        //        this.stockUpdateKafkaProducer = stockUpdateKafkaProducer;
    }

    /**
     * Add the stock for a given operator.
     *
     * @param operatorUsername The username of the operator.
     * @param stocks           The list of stocks to be updated.
     * @return ResponseEntity representing the response status.
     */
    @PostMapping()
    @MessageMapping("/updateStocks")
    @SendTo("/topic/stocks")
    public ResponseEntity<?> addStocks(@RequestParam(name = "operatorName") String operatorUsername, @RequestBody List<Stock> stocks) {
        LOGGER.info("inside /updateStocks api");
        // Find the operator by username
        Optional<User> operatorOptional = userService.findByUsername(operatorUsername);
        if (operatorOptional.isPresent()) {
            // Add stocks to the operator's stock scan and send stock updates through socket.io
            operatorStockService.addStocks(operatorOptional, stocks);
            LOGGER.warning("operator is present!");
            //TODO: Update the stock for the stock table
            for (Stock stock : stocks) {
                LOGGER.info("stock: " + stock.toString());
            }
            this.stockService.updateStocks(stocks);

            pusher.trigger("stocks", "update", stocks);
            return ResponseEntity.ok(stocks);
        } else {
            // Return error response if operator is not found
            ResponseError errorResponse = new ResponseError("Operator not found", 404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
