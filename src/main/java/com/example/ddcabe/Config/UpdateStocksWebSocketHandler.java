package com.example.ddcabe.Config;

import com.example.ddcabe.Stock.Stock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UpdateStocksWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void sendUpdatedDataToClients(List<Stock> updatedStocks) {
        ObjectMapper objectMapper = new ObjectMapper();
        String updatedStocksText;
        try {
            updatedStocksText = objectMapper.writeValueAsString(updatedStocks);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        }

        TextMessage message = new TextMessage(updatedStocksText);

        for (WebSocketSession clientSession : sessions.values()) {
            try {
                clientSession.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
    }
}
