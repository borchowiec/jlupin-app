package com.example.service.impl;

import com.example.bean.pojo.ChannelContext;
import com.example.common.pojo.Notification;
import com.example.service.interfaces.NotificationService;
import com.jlupin.impl.client.util.channel.JLupinClientChannelUtil;
import com.jlupin.impl.client.util.channel.exception.JLupinClientChannelUtilException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service(value = "notificationService")
public class NotificationServiceImpl implements NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private Set<ChannelContext> channelContexts = new HashSet<>();
    private String channelId;


    @Autowired
    private JLupinClientChannelUtil jLupinClientChannelUtil;

    @Override
    public void sendNotification(Notification notification) {
        logger.info("[SEND {}] after: {}", notification.toString(), channelContexts.toString());
        try {
            jLupinClientChannelUtil.putNextElementToStreamChannel(channelId, notification);
        } catch (JLupinClientChannelUtilException e) {
            logger.error("Cannot put element to {}.", channelId);
        }
        /*channelContexts.stream()
                .filter(context -> context.getUserId().equals(notification.getReceiver())) // send only to receivers
                .forEach(context -> {
                    try {
                        logger.info("[SEND] send: {}", context.getChannelId());
                        jLupinClientChannelUtil.putNextElementToStreamChannel(context.getChannelId(), notification);
                    } catch (JLupinClientChannelUtilException e) {
                        logger.error("Cannot put element to {}.", context.getChannelId());
                    }
                });*/
    }

    @Override
    public void addChannel(String userId, String sessionId, String channelId) {
        logger.info("[ADD {}] before: {}", sessionId, channelContexts.toString());
        channelContexts.add(new ChannelContext(channelId, userId, sessionId));
        logger.info("[ADD {}] after: {}", sessionId, channelContexts.toString());
    }

    @Override
    public void closeChannel(String sessionId) {
        logger.info("[CLOSE {}] before: {}", sessionId, channelContexts.toString());

        // get channel context by sessionId
        ChannelContext context = channelContexts.stream()
                .filter(ctx -> ctx.getSessionId().equals(sessionId))
                .findAny()
                .orElse(null);

        // close channel and remove channel context
        if (context != null) {
            try {
                jLupinClientChannelUtil.closeStreamChannel(context.getChannelId());
            } catch (JLupinClientChannelUtilException e) {
                logger.error("Cannot close channel of id {}", context.getChannelId());
            }
            channelContexts.remove(context);
        }

        logger.info("[CLOSE {}] after: {}", sessionId, channelContexts.toString());
    }

    @Override
    public void setChannel(String channelId) {
        this.channelId = channelId;
    }
}
