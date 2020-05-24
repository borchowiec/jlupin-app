package com.example.configuration;

import com.example.bean.impl.AuthenticationFilter;
import com.example.service.interfaces.MessageService;
import com.example.service.interfaces.SampleChannelWriter;
import com.example.service.interfaces.TaskService;
import com.example.service.interfaces.UserService;
import com.jlupin.impl.client.delegator.balance.JLupinQueueLoadBalancerDelegatorImpl;
import com.jlupin.impl.client.util.JLupinClientUtil;
import com.jlupin.impl.client.util.channel.JLupinClientChannelIterableProducer;
import com.jlupin.impl.client.util.channel.JLupinClientChannelUtil;
import com.jlupin.impl.client.util.queue.JLupinClientQueueUtil;
import com.jlupin.interfaces.client.delegator.JLupinDelegator;
import com.jlupin.interfaces.common.enums.PortType;
import com.jlupin.interfaces.microservice.partofjlupin.asynchronous.service.channel.JLupinChannelManagerService;
import com.jlupin.interfaces.microservice.partofjlupin.asynchronous.service.queue.JLupinQueueManagerService;
import com.jlupin.servlet.monitor.annotation.EnableJLupinSpringBootServletMonitor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.example")
@EnableJLupinSpringBootServletMonitor
public class RestApiSpringConfiguration {
    @Bean
    public JLupinDelegator getQueueJLupinDelegator() {
        final JLupinDelegator jLupinDelegator = JLupinClientUtil.generateInnerMicroserviceLoadBalancerDelegator(PortType.QUEUE);
        ((JLupinQueueLoadBalancerDelegatorImpl) jLupinDelegator).setGetStatusAnalyseAndChooseHighestFromAllEnvironment(true);
        return jLupinDelegator;
    }

    @Bean
    public JLupinQueueManagerService getJLupinQueueManagerService() {
        return JLupinClientUtil.generateRemote(getQueueJLupinDelegator(), "queueMicroservice", "jLupinQueueManagerService", JLupinQueueManagerService.class);
    }

    @Bean(name = "sampleQueueClientUtil")
    public JLupinClientQueueUtil getSampleQueueClientUtil() {
        return new JLupinClientQueueUtil("MESSAGES", getJLupinQueueManagerService());
    }



    @Bean(name = "sampleChannelWriter")
    public SampleChannelWriter getSampleChannelWriter() {
        return JLupinClientUtil.generateRemote(getJLupinDelegator(), "notification", SampleChannelWriter.class);
    }

    @Bean
    public JLupinChannelManagerService getJLupinChannelManagerService() {
        return JLupinClientUtil.generateRemote(getJLupinDelegator(), "channelMicroservice", "jLupinChannelManagerService", JLupinChannelManagerService.class);
    }

    @Bean(name="jLupinClientChannelUtil")
    public JLupinClientChannelUtil getJLupinClientChannelUtil() {
        JLupinChannelManagerService jLupinChannelManagerService = JLupinClientUtil.generateRemote(
                getJLupinDelegator(), "channelMicroservice", "jLupinChannelManagerService", JLupinChannelManagerService.class);

        return new JLupinClientChannelUtil("SAMPLE", jLupinChannelManagerService);
    }

    @Bean
    public JLupinClientChannelIterableProducer getJLupinClientChannelIterableProducer() {
        return new JLupinClientChannelIterableProducer(getJLupinChannelManagerService());
    }



    @Bean
    public JLupinDelegator getJLupinDelegator() {
        return JLupinClientUtil.generateInnerMicroserviceLoadBalancerDelegator(PortType.JLRMC);
    }

    @Bean(name = "userService")
    public UserService getUserService() {
        return JLupinClientUtil.generateRemote(getJLupinDelegator(), "user", UserService.class);
    }

    @Bean(name = "messageService")
    public MessageService getMessageService() {
        return JLupinClientUtil.generateRemote(getJLupinDelegator(), "message", MessageService.class);
    }

    @Bean(name = "taskService")
    public TaskService getTaskService() {
        return JLupinClientUtil.generateRemote(getJLupinDelegator(), "task", TaskService.class);
    }

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter() {
        FilterRegistrationBean<AuthenticationFilter> authenticationFilter = new FilterRegistrationBean<>();
        authenticationFilter.setFilter(new AuthenticationFilter(getUserService()));
        authenticationFilter.addUrlPatterns("/add-message", "/conversation/*", "/task", "/tasks", "/task/*");
        return authenticationFilter;
    }
}

