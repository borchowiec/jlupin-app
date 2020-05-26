package com.example.configuration;

import com.example.service.interfaces.MessageStorage;
import com.example.service.interfaces.NotificationService;
import com.example.service.interfaces.UserService;
import com.example.service.interfaces.UserStorage;
import com.jlupin.impl.client.util.JLupinClientUtil;
import com.jlupin.interfaces.client.delegator.JLupinDelegator;
import com.jlupin.interfaces.common.enums.PortType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan("com.example")
public class MessageSpringConfiguration {
    @Bean
    public JLupinDelegator getJLupinDelegator() {
        return JLupinClientUtil.generateInnerMicroserviceLoadBalancerDelegator(PortType.JLRMC);
    }

    @Bean("userStorage")
    public UserStorage getUserStorage() {
        return JLupinClientUtil.generateRemote(getJLupinDelegator(), "user-storage", UserStorage.class);
    }

    @Bean(name = "userService")
    public UserService getUserService() {
        return JLupinClientUtil.generateRemote(getJLupinDelegator(), "user", UserService.class);
    }

    @Bean(name = "messageStorage")
    public MessageStorage getMessageStorage() {
        return JLupinClientUtil.generateRemote(getJLupinDelegator(), "message-storage", MessageStorage.class);
    }

    @Bean(name = "notificationService")
    public NotificationService getNotificationService() {
        return JLupinClientUtil.generateRemote(getJLupinDelegator(), "notification", NotificationService.class);
    }

    @Bean(name = "jLupinRegularExpressionToRemotelyEnabled")
    public List getRemotelyBeanList() {
        List<String> list = new ArrayList<>();
        list.add("messageService");
        // list.add("<REMOTE_SERVICE_NAME>");
        return list;
    }
}

