package com.example.configuration;

import com.example.bean.impl.AuthenticationFilter;
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

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter(){
        FilterRegistrationBean<AuthenticationFilter> authenticationFilter = new FilterRegistrationBean<>();
        authenticationFilter.setFilter(new AuthenticationFilter(getUserService()));
        authenticationFilter.addUrlPatterns("/add-message");
        return authenticationFilter;
    }
}

