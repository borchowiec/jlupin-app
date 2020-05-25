package com.example.configuration;

import com.example.websocket.handler.NotificationWebSocketHandlerImpl;
import com.example.websocket.handler.WebSocketHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {
  @Autowired
  private NotificationWebSocketHandlerImpl notificationWebSocketHandler;

  @Override
  public void registerWebSocketHandlers(final WebSocketHandlerRegistry webSocketHandlerRegistry) {
    webSocketHandlerRegistry
            .addHandler(notificationWebSocketHandler, "/notifications")
            .setAllowedOrigins("*");
  }
}