//package com.example.ddcabe.Kafka;
//
//import com.example.ddcabe.OperatorStock.OperatorStockService;
//import com.example.ddcabe.Socket.SocketIOController;
//import com.example.ddcabe.Stock.Stock;
//import com.example.ddcabe.User.User;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@Slf4j
//public class Consumer {
//
//    private final OperatorStockService operatorStockService;
//    private final SocketIOController socketIOController;
//
//    /**
//     * Constructs a Consumer object with the given dependencies.
//     *
//     * @param operatorStockService an instance of OperatorStockScanService used to add the stock to the database.
//     * @param socketIOController       an instance of SocketIOController used to send stock update notifications.
//     */
//    public Consumer(OperatorStockService operatorStockService, SocketIOController socketIOController) {
//        this.operatorStockService = operatorStockService;
//        this.socketIOController = socketIOController;
//    }
//
//    /**
//     * Kafka listener method that processes stock update messages received from the "stock-update" topic.
//     *
//     * @param operator an optional User representing the operator, if available.
//     * @param stock    the Stock object representing the updated stock.
//     */
//    @KafkaListener(topics = "stock-update", groupId = "stock-update-group")
//    public void processStockUpdate(Optional<User> operator, Stock stock) {
//        log.info(String.format("#### -> Consumed stock -> %s", stock));
//
//        // Save the received stock message to the database.
//        this.operatorStockService.addSingleStock(operator, stock);
//
//        // Send the stock update to the SocketIO controller for further processing.
//        socketIOController.sendStockUpdate(stock);
//
//        log.info(String.format("#### -> New stock updated -> %s", stock.getItemId()));
//    }
//}