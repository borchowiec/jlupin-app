package com.example.websocket.handler;

import com.example.common.util.JwtTokenProvider;
import com.example.common.util.StringUtils;
import com.example.service.interfaces.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jlupin.impl.client.util.channel.JLupinClientChannelIterableProducer;
import com.jlupin.impl.client.util.channel.JLupinClientChannelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component(value = "notificationWebSocketHandler")
public class NotificationWebSocketHandlerImpl extends TextWebSocketHandler {

    @Autowired
    private JLupinClientChannelUtil jLupinClientChannelUtil;

    @Autowired
    private JLupinClientChannelIterableProducer jLupinClientChannelIterableProducer;

    @Autowired
    @Qualifier("notificationService")
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    private JwtTokenProvider tokenProvider = JwtTokenProvider.getInstance();

    private static final Logger logger = LoggerFactory.getLogger(NotificationWebSocketHandlerImpl.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String stringCookies = session.getHandshakeHeaders().toSingleValueMap().get("cookie");
        String token = StringUtils.getValueFromCookies(stringCookies, "Authorization");

        if (token != null) {
            String userId = tokenProvider.getId(token);
            String chanelId = jLupinClientChannelUtil.openStreamChannel();

            logger.info("[connect {}] {}", session.getId(), chanelId);

            notificationService.addChannel(userId, session.getId(), chanelId);
            Iterable iterable = jLupinClientChannelIterableProducer.produceChannelIterable("SAMPLE", chanelId);

            new Thread(() -> {
                for (Object notification : iterable) {
                    try {
                        logger.info("[receive]: {}", chanelId);
                        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(notification)));
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                    }
                }
            }).start();
        }
        else {
            session.close();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //notificationService.closeChannel(session.getId());
        //logger.info("[disconnect {}]", session.getId());
    }
}
