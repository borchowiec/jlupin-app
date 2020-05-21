package com.example.configuration;

import com.example.bean.impl.AuthenticationFilter;
import com.example.service.interfaces.MessageService;
import com.example.service.interfaces.TaskService;
import com.example.service.interfaces.UserService;
import com.jlupin.impl.client.util.JLupinClientUtil;
import com.jlupin.interfaces.client.delegator.JLupinDelegator;
import com.jlupin.interfaces.common.enums.PortType;
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

