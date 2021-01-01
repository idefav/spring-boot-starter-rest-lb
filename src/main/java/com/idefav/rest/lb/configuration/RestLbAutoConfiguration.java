package com.idefav.rest.lb.configuration;

import com.idefav.rest.lb.LoadBalanced;
import com.idefav.rest.lb.interceptors.LoadBalancerInterceptor;
import com.idefav.rest.lb.properties.ServerListProperty;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * RestTemplate LoadBanlencer 自动配置
 *
 * @author wuzishu
 */
@EnableConfigurationProperties(ServerListProperty.class)
public class RestLbAutoConfiguration {

    @LoadBalanced
    @Autowired(required = false)
    private List<RestTemplate> restTemplates = Collections.emptyList();

    /**
     * Load balancer interceptor load balancer interceptor.
     *
     * @param serverListProperty the server list property
     * @return the load balancer interceptor
     */
    @Bean
    public LoadBalancerInterceptor loadBalancerInterceptor(ServerListProperty serverListProperty) {
        return new LoadBalancerInterceptor(serverListProperty.getServiceList());
    }

    /**
     * Load balanced rest template initializer deprecated smart initializing singleton.
     *
     * @param restTemplateCustomizers the rest template customizers
     * @return the smart initializing singleton
     */
    @Bean
    public SmartInitializingSingleton loadBalancedRestTemplateInitializerDeprecated(
            final ObjectProvider<List<RestTemplateCustomizer>> restTemplateCustomizers) {
        return () -> restTemplateCustomizers.ifAvailable(customizers -> {
            for (RestTemplate restTemplate : RestLbAutoConfiguration.this.restTemplates) {
                for (RestTemplateCustomizer customizer : customizers) {
                    customizer.customize(restTemplate);
                }
            }
        });
    }

    /**
     * Rest template customizer rest template customizer.
     *
     * @param loadBalancerInterceptor the load balancer interceptor
     * @return the rest template customizer
     */
    @Bean
    @ConditionalOnMissingBean
    public RestTemplateCustomizer restTemplateCustomizer(
            final LoadBalancerInterceptor loadBalancerInterceptor) {
        return restTemplate -> {
            List<ClientHttpRequestInterceptor> list = new ArrayList<>(
                    restTemplate.getInterceptors());
            list.add(loadBalancerInterceptor);
            restTemplate.setInterceptors(list);
        };
    }
}
