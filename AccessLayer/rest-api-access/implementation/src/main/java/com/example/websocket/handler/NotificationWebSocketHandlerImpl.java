package com.example.websocket.handler;

import com.example.common.pojo.Notification;
import com.example.common.util.JwtTokenProvider;
import com.example.common.util.StringUtils;
import com.example.service.interfaces.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jlupin.impl.client.util.channel.JLupinClientChannelIterableProducer;
import com.jlupin.impl.client.util.channel.JLupinClientChannelUtil;
import com.jlupin.impl.client.util.channel.exception.JLupinClientChannelUtilException;
import javafx.util.Pair;
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
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

    private String CHANNEL_ID;

    /**
     * Pair(userId, session)
     */
    private List<Pair<String, WebSocketSession>> sessions = Collections.synchronizedList(new LinkedList<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (CHANNEL_ID == null) {
            CHANNEL_ID = jLupinClientChannelUtil.openStreamChannel();
            notificationService.setChannel(CHANNEL_ID);
        }

        String stringCookies = session.getHandshakeHeaders().toSingleValueMap().get("cookie");
        String token = StringUtils.getValueFromCookies(stringCookies, "Authorization");

        if (token != null) {
            String userId = tokenProvider.getId(token);
            sessions.add(new Pair<>(userId, session));
            logger.info("[connect {}] {}", session.getId(), userId);
            logger.info("[SESSIONS] {}", sessions.toString());

            new Thread(() -> {
                Iterable iterable = jLupinClientChannelIterableProducer
                        .produceChannelIterable("SAMPLE", CHANNEL_ID);
                Iterator iterator = iterable.iterator();

                while (iterator.hasNext()) {
                    Notification notification = (Notification) iterator.next();
                    logger.info("loop " + Thread.currentThread().getName());
                    logger.info("SESS SIZE {}", sessions.size());
                    for (Pair<String, WebSocketSession> pair: sessions) {
                        try {
                            if (pair.getValue().isOpen() && notification.getReceiver().equals(pair.getKey())) {
                                logger.info("[receive]: {} - {}", pair.getKey(), pair.getValue().getId());
                                pair.getValue().sendMessage(new TextMessage(objectMapper.writeValueAsString(notification)));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
