package com.example.websocket.handler;

import com.example.common.util.StringUtils;
import com.example.service.interfaces.NotificationService;
import com.example.service.interfaces.UserService;
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

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(NotificationWebSocketHandlerImpl.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String stringCookies = session.getHandshakeHeaders().toSingleValueMap().get("cookie");
        String token = StringUtils.getValueFromCookies(stringCookies, "Authorization");

        if (token != null) {
            long userId = userService.getUserIdFromToken(token);
            String chanelId = jLupinClientChannelUtil.openStreamChannel();

            notificationService.addChannel(userId, session.getId(), chanelId);
            Iterable iterable = jLupinClientChannelIterableProducer.produceChannelIterable("SAMPLE", chanelId);

            for (Object notification : iterable) {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(notification)));
            }
        }
        else {
            session.close();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        notificationService.closeChannel(session.getId());
    }
}
