package com.example.configuration;

import com.jlupin.impl.client.util.JLupinClientUtil;
import com.jlupin.impl.client.util.channel.JLupinClientChannelUtil;
import com.jlupin.interfaces.client.delegator.JLupinDelegator;
import com.jlupin.interfaces.common.enums.PortType;
import com.jlupin.interfaces.microservice.partofjlupin.asynchronous.service.channel.JLupinChannelManagerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan("com.example")
public class NotificationSpringConfiguration {
    @Bean
    public JLupinDelegator getJLupinDelegator() {
        return JLupinClientUtil.generateInnerMicroserviceLoadBalancerDelegator(PortType.JLRMC);
    }

    @Bean(name="jLupinClientChannelUtil")
    public JLupinClientChannelUtil getJLupinClientChannelUtil() {
        JLupinChannelManagerService jLupinChannelManagerService = JLupinClientUtil.generateRemote(
                getJLupinDelegator(), "channelMicroservice", "jLupinChannelManagerService", JLupinChannelManagerService.class);

        return new JLupinClientChannelUtil("SAMPLE", jLupinChannelManagerService);
    }

    @Bean(name = "jLupinRegularExpressionToRemotelyEnabled")
    public List getRemotelyBeanList() {
        List<String> list = new ArrayList<>();
        list.add("sampleChannelWriter");
        return list;
    }
}

