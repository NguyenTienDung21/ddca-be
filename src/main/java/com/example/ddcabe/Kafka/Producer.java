//package com.example.ddcabe.Kafka;
//
//import com.example.ddcabe.Stock.Stock;
//import com.example.ddcabe.User.User;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//public class Producer {
//    private static final String TOPIC = "stock-update";
//
//    private final KafkaTemplate<String, Stock> kafkaTemplate;
//
//    public Producer(KafkaTemplate<String, Stock> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    /**
//     * Sends a stock update message to Kafka.
//     *
//     * @param operator The user performing the update.
//     * @param stock    The updated stock information.
//     */
//    public void sendStockUpdate(User operator, Stock stock) {
//        log.info(String.format("#### -> updating new stock -> %s", stock.getItemId(), " by operator -> %s", operator.getUsername()));
//        this.kafkaTemplate.send(TOPIC, stock);
//    }
//}
