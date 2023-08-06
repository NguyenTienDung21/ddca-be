package com.example.ddcabe.Socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.ddcabe.Stock.Stock;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SocketIOController {
    private final SocketIOServer socketIOServer;

    public SocketIOController(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
    }

    /**
     * Event listener method triggered when the application context is refreshed.
     * It starts the SocketIO server and adds listeners for client connection and disconnection.
     *
     * @param event The ContextRefreshedEvent indicating the application context has been refreshed.
     */
    @EventListener
    public void onStockUpdateEvent(ContextRefreshedEvent event) {
        log.info("Event listener method called!");
        socketIOServer.start();

        socketIOServer.addConnectListener(client -> {
            //TODO: handle client connection here
            log.info("Client connected: " + client.getSessionId());
        });

        socketIOServer.addDisconnectListener(client -> {
            //TODO: handle client disconnection here
            log.info("Client disconnected: " + client.getSessionId());
        });
    }


    /**
     * Clean-up method to stop the SocketIO server when the component is being destroyed.
     */
    @PreDestroy
    public void onDestroy() {
        socketIOServer.stop();
    }

    /**
     * Sends a stock update to all connected clients.
     *
     * @param stock The Stock object representing the updated stock information.
     */
    public void sendStockUpdate(Stock stock) {
        socketIOServer.getBroadcastOperations().sendEvent("stockUpdate", stock);
    }
}


