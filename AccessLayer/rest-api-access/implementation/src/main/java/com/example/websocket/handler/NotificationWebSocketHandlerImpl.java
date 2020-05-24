package com.example.websocket.handler;

import com.jlupin.impl.client.util.channel.JLupinClientChannelIterableProducer;
import com.jlupin.impl.client.util.channel.JLupinClientChannelUtil;
import com.jlupin.impl.client.util.channel.exception.JLupinClientChannelUtilException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(value = "notificationWebSocketHandler")
public class NotificationWebSocketHandlerImpl extends TextWebSocketHandler {

    @Autowired
    private JLupinClientChannelUtil jLupinClientChannelUtil;

    @Autowired
    private JLupinClientChannelIterableProducer jLupinClientChannelIterableProducer;

    private Map<String, WebSocketSession> sessions = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(NotificationWebSocketHandlerImpl.class);
    private String temp_channel_id;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        temp_channel_id = jLupinClientChannelUtil.openStreamChannel();
        sessions.put(session.getId(), session);
        logger.info("afterConnection {}", session.getId());
        logger.info("channelId " + temp_channel_id);

        Iterable iterable = jLupinClientChannelIterableProducer.produceChannelIterable("SAMPLE", temp_channel_id);

        for (Object notification : iterable) {
            logger.info("[NOTIFICATION] " + notification);
        }
    }

    public void sendNotification() {
        try {
            jLupinClientChannelUtil.putNextElementToStreamChannel(temp_channel_id, "teeest");
        } catch (JLupinClientChannelUtilException e) {
            e.printStackTrace();
        }
    }
}
