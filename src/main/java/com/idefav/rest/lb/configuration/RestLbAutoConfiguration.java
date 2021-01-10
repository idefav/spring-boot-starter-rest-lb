package com.idefav.rest.lb.configuration;

import com.idefav.rest.lb.LbServer;
import com.idefav.rest.lb.LoadBalanced;
import com.idefav.rest.lb.ServiceConfig;
import com.idefav.rest.lb.interceptors.LoadBalancerInterceptor;
import com.idefav.rest.lb.properties.RetryProperty;
import com.idefav.rest.lb.properties.ServerListProperty;
import com.idefav.rest.lb.properties.ServiceProperty;
import com.idefav.rest.lb.recovery.AbstractRecoveryCallback;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.*;
import org.springframework.retry.policy.CircuitBreakerRetryPolicy;
import org.springframework.retry.policy.CompositeRetryPolicy;
import org.springframework.retry.policy.MaxAttemptsRetryPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

/**
 * RestTemplate LoadBalancer 自动配置
 *
 * @author wuzishu
 */
@EnableConfigurationProperties(ServerListProperty.class)
public class RestLbAutoConfiguration {

    @LoadBalanced
    @Autowired(required = false)
    private List<RestTemplate> restTemplates = Collections.emptyList();

    /**
     * Retry template map map.
     *
     * @param serverListProperty the server list property
     * @return the map
     */
    @Bean
    @ConditionalOnClass(RetryTemplate.class)
    public Map<String, RetryTemplate> retryTemplateMap(ServerListProperty serverListProperty) {
        Map<String, RetryTemplate> retryTemplateMap = new HashMap<>();
        Map<String, ServiceProperty> serviceList = serverListProperty.getServiceList();
        serviceList.forEach((k, v) -> {
            if (v.getRetryProperty() != null && v.getRetryProperty().isEnable()) {
                RetryProperty retryProperty = v.getRetryProperty();
                RetryTemplate retryTemplate = new RetryTemplate();

                List<RetryPolicy> retryPolicies = new ArrayList<>();

                // 如果设置重试策略
                if (retryProperty.getRetry() != null) {
                    // 设置最大重试次数
                    if (retryProperty.getRetry().getMaxAttempts() != null && retryProperty.getRetry().getMaxAttempts() > 0) {
                        retryPolicies.add(new MaxAttemptsRetryPolicy(retryProperty.getRetry().getMaxAttempts()));
                    }

                    // 重试最大超时时间
                    if (retryProperty.getRetry().getTimeout() != null && retryProperty.getRetry().getTimeout() > 0) {
                        TimeoutRetryPolicy timeoutRetryPolicy = new TimeoutRetryPolicy();
                        timeoutRetryPolicy.setTimeout(retryProperty.getRetry().getTimeout());
                        retryPolicies.add(timeoutRetryPolicy);
                    }

                    // 熔断重试
                    if (retryProperty.getRetry().getOpenTimeout() != null && retryProperty.getRetry().getOpenTimeout() > 0
                            && retryProperty.getRetry().getResetTimeout() != null && retryProperty.getRetry().getResetTimeout() > 0) {
                        CircuitBreakerRetryPolicy circuitBreakerRetryPolicy = new CircuitBreakerRetryPolicy();
                        circuitBreakerRetryPolicy.setOpenTimeout(retryProperty.getRetry().getOpenTimeout());
                        circuitBreakerRetryPolicy.setResetTimeout(retryProperty.getRetry().getResetTimeout());
                        retryPolicies.add(circuitBreakerRetryPolicy);
                    }
                }
                BackOffPolicy backOffPolicy = new NoBackOffPolicy();
                if (retryProperty.getBackOff() != null) {
                    RetryProperty.BackOff backOff = retryProperty.getBackOff();
                    switch (backOff.getBackOff()) {
                        case FIXED_BACKOFF:
                            FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
                            if (backOff.getBackOffPeriod() != null && backOff.getBackOffPeriod() > 0) {
                                fixedBackOffPolicy.setBackOffPeriod(backOff.getBackOffPeriod());
                            }
                            backOffPolicy = fixedBackOffPolicy;
                            break;
                        case UNIFORM_RANDOM_BACKOFF:
                            UniformRandomBackOffPolicy uniformRandomBackOffPolicy = new UniformRandomBackOffPolicy();
                            if (backOff.getBackOffPeriod() != null && backOff.getBackOffPeriod() > 0) {
                                uniformRandomBackOffPolicy.setMinBackOffPeriod(backOff.getBackOffPeriod());
                            }
                            if (backOff.getMaxBackOffPeriod() != null && backOff.getMaxBackOffPeriod() > 0) {
                                uniformRandomBackOffPolicy.setMaxBackOffPeriod(backOff.getMaxBackOffPeriod());
                            }
                            backOffPolicy = uniformRandomBackOffPolicy;
                            break;
                        case EXPONENTIAL_BACKOFF:
                            ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
                            if (backOff.getInitialInterval() != null && backOff.getInitialInterval() > 0) {
                                exponentialBackOffPolicy.setInitialInterval(backOff.getInitialInterval());
                            }
                            if (backOff.getMaxInterval() != null && backOff.getMaxInterval() > 0) {
                                exponentialBackOffPolicy.setMaxInterval(backOff.getMaxInterval());
                            }
                            if (backOff.getMultiplier() != null && backOff.getMultiplier() > 0) {
                                exponentialBackOffPolicy.setMultiplier(backOff.getMultiplier());
                            }
                            backOffPolicy = exponentialBackOffPolicy;
                            break;
                        case EXPONENTIAL_RANDOM_BACKOFF:
                            ExponentialRandomBackOffPolicy exponentialRandomBackOffPolicy = new ExponentialRandomBackOffPolicy();
                            if (backOff.getInitialInterval() != null && backOff.getInitialInterval() > 0) {
                                exponentialRandomBackOffPolicy.setInitialInterval(backOff.getInitialInterval());
                            }
                            if (backOff.getMaxInterval() != null && backOff.getMaxInterval() > 0) {
                                exponentialRandomBackOffPolicy.setMaxInterval(backOff.getMaxInterval());
                            }
                            if (backOff.getMultiplier() != null && backOff.getMultiplier() > 0) {
                                exponentialRandomBackOffPolicy.setMultiplier(backOff.getMultiplier());
                            }
                            backOffPolicy = exponentialRandomBackOffPolicy;
                            break;
                        case NO_BACKOFF:
                        default:
                            backOffPolicy = new NoBackOffPolicy();

                    }
                    retryTemplate.setBackOffPolicy(backOffPolicy);
                }

                // 设置组合重试策略
                if (retryPolicies.size() > 0) {
                    CompositeRetryPolicy compositeRetryPolicy = new CompositeRetryPolicy();
                    compositeRetryPolicy.setPolicies(retryPolicies.toArray(new RetryPolicy[0]));

                    retryTemplate.setRetryPolicy(compositeRetryPolicy);
                }

                retryTemplateMap.put(k, retryTemplate);
            }
        });
        return retryTemplateMap;
    }

/*    @Autowired(required = false)
    private Map<String, RetryTemplate> retryTemplateMap;*/

    /**
     * Service config map.
     *
     * @param serverListProperty the server list property
     * @return the map
     */
    @Bean
    @Qualifier("serviceConfig")
    public Map<String, ServiceConfig> serviceConfig(ServerListProperty serverListProperty, Map<String, RetryTemplate> retryTemplateMap) {
        Map<String, ServiceConfig> serviceConfigMap = new HashMap<>();
        Map<String, ServiceProperty> serviceList = serverListProperty.getServiceList();
        if (serviceList == null || serviceList.isEmpty()) {
            return serviceConfigMap;
        }
        serviceList.forEach((k, v) -> {
            ServiceConfig serviceConfig = new ServiceConfig();
            serviceConfig.setServerList(v.getServerList().stream().map(c -> {
                LbServer lbServer = new LbServer();
                lbServer.setProperties(c.getProperties());
                lbServer.setUrl(c.getUrl());
                lbServer.setWeight(c.getWeight());
                return lbServer;
            }).collect(Collectors.toList()));

            // 设置重试
            if (retryTemplateMap != null && retryTemplateMap.size() > 0) {
                RetryTemplate retryTemplate = retryTemplateMap.get(k);
                serviceConfig.setRetryTemplate(retryTemplate);

                // 重试失败, 降级处理逻辑
                if (v.getRetryProperty() != null) {
                    serviceConfig.setCondition(v.getRetryProperty().getCondition());
                    Class<? extends AbstractRecoveryCallback> recoveryCallback = v.getRetryProperty().getRecoveryCallback();
                    if (recoveryCallback != null) {
                        try {
                            serviceConfig.setRecoveryCallback(recoveryCallback.newInstance());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

            serviceConfig.setLoadBalancor(v.getLoadBalancor());
            serviceConfigMap.put(k, serviceConfig);
        });
        return serviceConfigMap;
    }

    /**
     * Load balancer interceptor load balancer interceptor.
     *
     * @param serviceConfigMap the service config map
     * @return the load balancer interceptor
     */
    @Bean
    public LoadBalancerInterceptor loadBalancerInterceptor(@Qualifier("serviceConfig") Map<String, ServiceConfig> serviceConfigMap) {
        return new LoadBalancerInterceptor(serviceConfigMap);
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
