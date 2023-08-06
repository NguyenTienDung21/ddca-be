package com.example.ddcabe.ScheduleTask;

import java.time.LocalDateTime;

import com.example.ddcabe.OperatorStock.OperatorStockRepository;
import com.example.ddcabe.Session.SessionRepository;
import com.example.ddcabe.Stock.StockRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;

@Service
public class DataCleanUpService {

    private static final int DAYS_THRESHOLD = 7;


    private final SessionRepository sessionRepository;

    private final StockRepository stockRepository;

    private final OperatorStockRepository operatorStockRepository;

    public DataCleanUpService(SessionRepository sessionRepository, StockRepository stockRepository, OperatorStockRepository operatorStockRepository) {
        this.sessionRepository = sessionRepository;
        this.stockRepository = stockRepository;
        this.operatorStockRepository = operatorStockRepository;
    }

    /**
     * Clean up data older than a week
     * Runs at 6.15 AM every day
     * This method is used to clean up data older than a week.
     * It deletes sessions, stocks and operator stocks older than a week.
     */
    @Scheduled(cron = "0 15 6 * * ?")
    public void cleanupOldData() {
        // Calculate the threshold date
        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(DAYS_THRESHOLD);

        // Delete sessions older than the threshold date
        sessionRepository.deleteByCreatedAtBefore(thresholdDate);

        // Delete stocks older than the threshold date
        stockRepository.deleteByCreatedAtBefore(thresholdDate);

        // Delete operator stocks older than the threshold date
        operatorStockRepository.deleteByCreatedAtBefore(thresholdDate);
    }
}
