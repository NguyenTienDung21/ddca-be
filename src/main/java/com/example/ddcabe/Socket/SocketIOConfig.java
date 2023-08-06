package com.example.ddcabe.Socket;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import com.corundumstudio.socketio.Configuration;

/**
 * Configuration class for Socket.IO server.
 */
@org.springframework.context.annotation.Configuration
@Slf4j
public class SocketIOConfig {
    // Injecting values from application.properties using @Value annotation
    @Value("${server.host}")
    private String host;

    @Value("${socket.port}")
    private Integer port;

    /**
     * Creates and configures the Socket.IO server bean.
     *
     * @return SocketIOServer instance
     */
    @Bean
    public SocketIOServer socketIOServer() {
        log.debug("Starting socket.io server at {}:{}", host, port);

        // Creating a new configuration instance for Socket.IO server
        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);

        // Initializing and returning the Socket.IO server
        return new SocketIOServer(config);
    }
}

