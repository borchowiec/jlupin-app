package com.example.websocket.handler;

import com.example.common.pojo.Notification;
import com.example.common.util.JwtTokenProvider;
import com.example.common.util.StringUtils;
import com.example.service.interfaces.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jlupin.impl.client.util.channel.JLupinClientChannelIterableProducer;
import com.jlupin.impl.client.util.channel.JLupinClientChannelUtil;
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

    private String channelId;

    /**
     * Pair(userId, session)
     */
    private List<Pair<String, WebSocketSession>> sessions = Collections.synchronizedList(new LinkedList<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession newSession) throws Exception {
        // open channel
        if (channelId == null) {
            channelId = jLupinClientChannelUtil.openStreamChannel();
            notificationService.setChannel(channelId);
        }

        // get user token
        String stringCookies = newSession.getHandshakeHeaders().toSingleValueMap().get("cookie");
        String token = StringUtils.getValueFromCookies(stringCookies, "Authorization");

        if (token != null) {
            // put save session data
            String userId = tokenProvider.getId(token);
            sessions.add(new Pair<>(userId, newSession));

            // listen for notifications
            new Thread(() -> {
                Iterable iterable = jLupinClientChannelIterableProducer
                        .produceChannelIterable("SAMPLE", channelId);
                Iterator iterator = iterable.iterator();

                while (iterator.hasNext()) {
                    Notification notification = (Notification) iterator.next();
                    for (Pair<String, WebSocketSession> pair: sessions) {
                        String sessionUser = pair.getKey();
                        WebSocketSession session = pair.getValue();
                        try {
                            if (session.isOpen() && notification.getReceiver().equals(sessionUser)) {
                                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(notification)));
                            }
                            else if (!session.isOpen()) {
                                sessions.remove(pair);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
        else {
            newSession.close();
        }
    }
}
