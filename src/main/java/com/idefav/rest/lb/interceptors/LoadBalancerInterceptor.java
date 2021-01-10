package com.idefav.rest.lb.interceptors;


import com.idefav.rest.lb.LbServer;
import com.idefav.rest.lb.RestUtil;
import com.idefav.rest.lb.ServiceConfig;
import com.idefav.rest.lb.loadbalancers.LoadBalancer;
import com.idefav.rest.lb.loadbalancers.RandomLoadBalaner;
import com.idefav.rest.lb.properties.RetryProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the LoadBalancerInterceptor description.
 *
 * @author wuzishu
 */
public class LoadBalancerInterceptor implements ClientHttpRequestInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(LoadBalancerInterceptor.class);

    private final static Object lock = new Object();

    private static final Map<String, LoadBalancer> serviceLoadBancorList = new HashMap<>();

    /**
     * The constant RETRY_CONTEXT_ATTRIBUTE_REQUEST.
     */
    public static final String RETRY_CONTEXT_ATTRIBUTE_REQUEST = "request";
    /**
     * The constant RETRY_CONTEXT_ATTRIBUTE_BODY.
     */
    public static final String RETRY_CONTEXT_ATTRIBUTE_BODY = "body";
    /**
     * The constant RETRY_CONTEXT_ATTRIBUTE_EXECUTION.
     */
    public static final String RETRY_CONTEXT_ATTRIBUTE_EXECUTION = "clientHttpRequestExecution";
    /**
     * The constant RETRY_CONTEXT_ATTRIBUTE_SERVICECONFIG.
     */
    public static final String RETRY_CONTEXT_ATTRIBUTE_SERVICECONFIG = "serviceConfig";
    /**
     * The constant RETRY_CONTEXT_ATTRIBUTE_SERVICELIST.
     */
    public static final String RETRY_CONTEXT_ATTRIBUTE_SERVICELIST = "serviceList";
    /**
     * The constant RETRY_CONTEXT_ATTRIBUTE_SERVICEID.
     */
    public static final String RETRY_CONTEXT_ATTRIBUTE_SERVICEID = "serviceId";
    public static final String RETRY_CONTEXT_ATTRIBUTE_RESPONSE = "response";

    private Map<String, ServiceConfig> serviceList = new HashMap<>();

    /**
     * Instantiates a new Load balancer interceptor.
     *
     * @param serviceList the service list
     */
    public LoadBalancerInterceptor(Map<String, ServiceConfig> serviceList) {
        this.serviceList = serviceList;
    }

    /**
     * Gets loadbalancer.
     *
     * @param serviceId the service id
     * @return the loadbalancer
     */
    public LoadBalancer getLoadbalancer(String serviceId) {
        if (serviceLoadBancorList.containsKey(serviceId)) {
            return serviceLoadBancorList.get(serviceId);
        }
        synchronized (lock) {
            if (serviceLoadBancorList.containsKey(serviceId)) {
                return serviceLoadBancorList.get(serviceId);
            }
            ServiceConfig serviceConfig = serviceList.get(serviceId);
            LoadBalancer newInstance;
            try {
                Constructor<?> constructor = serviceConfig.getLoadBalancor().getConstructor(List.class);
                newInstance = (LoadBalancer) constructor.newInstance(serviceConfig.getServerList());
                serviceLoadBancorList.put(serviceId, newInstance);
                return newInstance;
            } catch (Exception e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("create loadbalancer instance failed, " + ExceptionUtils.getMessage(e));
                }
                newInstance = new RandomLoadBalaner(serviceList.get(serviceId).getServerList());
            }
            serviceLoadBancorList.put(serviceId, newInstance);
            return newInstance;
        }
    }

    /**
     * Gets retry template.
     *
     * @param serviceId the service id
     * @return the retry template
     */
    public RetryTemplate getRetryTemplate(String serviceId) {
        ServiceConfig serviceConfig = serviceList.get(serviceId);
        if (serviceConfig != null)
            return serviceConfig.getRetryTemplate();
        return null;
    }

    /**
     * Gets recovery callback.
     *
     * @param serviceId the service id
     * @return the recovery callback
     */
    public RecoveryCallback<ClientHttpResponse> getRecoveryCallback(String serviceId) {
        ServiceConfig serviceConfig = serviceList.get(serviceId);
        if (serviceConfig != null)
            return serviceConfig.getRecoveryCallback();
        return null;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        LoadBalancerHttpReqeustWrapper reqeustWrapper = new LoadBalancerHttpReqeustWrapper(httpRequest);
        RetryTemplate retryTemplate = getRetryTemplate(reqeustWrapper.getServiceId());
        if (retryTemplate == null) {
            return clientHttpRequestExecution.execute(reqeustWrapper, bytes);
        }
        try {
            if (getRecoveryCallback(reqeustWrapper.getServiceId()) != null) {
                return retryTemplate.execute((RetryCallback<ClientHttpResponse, Throwable>) context -> proc(context, reqeustWrapper, bytes, clientHttpRequestExecution), getRecoveryCallback(reqeustWrapper.getServiceId()));
            } else {
                return retryTemplate.execute((RetryCallback<ClientHttpResponse, Throwable>) context -> proc(context, reqeustWrapper, bytes, clientHttpRequestExecution));
            }

        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    private ClientHttpResponse proc(RetryContext context, LoadBalancerHttpReqeustWrapper reqeustWrapper, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        context.setAttribute(RETRY_CONTEXT_ATTRIBUTE_REQUEST, reqeustWrapper);
        context.setAttribute(RETRY_CONTEXT_ATTRIBUTE_BODY, bytes);
        context.setAttribute(RETRY_CONTEXT_ATTRIBUTE_EXECUTION, clientHttpRequestExecution);
        context.setAttribute(RETRY_CONTEXT_ATTRIBUTE_SERVICELIST, serviceList);
        context.setAttribute(RETRY_CONTEXT_ATTRIBUTE_SERVICEID, reqeustWrapper.getServiceId());
        ServiceConfig serviceConfig = serviceList.get(reqeustWrapper.getServiceId());
        context.setAttribute(RETRY_CONTEXT_ATTRIBUTE_SERVICECONFIG, serviceConfig);
        ClientHttpResponse execute = clientHttpRequestExecution.execute(reqeustWrapper, bytes);
        context.setAttribute(RETRY_CONTEXT_ATTRIBUTE_RESPONSE, execute);
        if (serviceConfig.getCondition() == null) {
            return execute;
        }
        List<HttpStatus> needRetryStatusCodes = Arrays.asList(serviceConfig.getCondition().getNeedRetryStatusCodes());
        List<HttpMethod> needRetryHttpMethods = Arrays.asList(serviceConfig.getCondition().getNeedRetryHttpMethods());
        if (needRetryHttpMethods.contains(reqeustWrapper.getMethod()) && needRetryStatusCodes.contains(execute.getStatusCode())) {
            throw new RuntimeException("request failed with error status code:" + execute.getStatusCode().value());
        } else {
            return execute;
        }

    }

    /**
     * The type Load balancer http reqeust wrapper.
     */
    public class LoadBalancerHttpReqeustWrapper extends HttpRequestWrapper {

        private LbServer choosedServer = null;

        private String serviceId = StringUtils.EMPTY;

        /**
         * Gets choosed server.
         *
         * @return the choosed server
         */
        public LbServer getChoosedServer() {
            return choosedServer;
        }

        /**
         * Gets service id.
         *
         * @return the service id
         */
        public String getServiceId() {
            if (StringUtils.isEmpty(this.serviceId)) {
                URI oldUri = super.getURI();
                this.serviceId = oldUri.getHost();
            }
            return serviceId;
        }

        /**
         * Instantiates a new Load balancer http reqeust wrapper.
         *
         * @param request the request
         */
        public LoadBalancerHttpReqeustWrapper(HttpRequest request) {
            super(request);
        }


        @Override
        public URI getURI() {
            URI oldUri = super.getURI();
            this.serviceId = oldUri.getHost();
            LoadBalancer loadBancor = getLoadbalancer(this.serviceId);
            LbServer server = loadBancor.getServer();
            choosedServer = server;
            try {
                return RestUtil.replaceHost(oldUri, server.getUrl());
            } catch (URISyntaxException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("replace host fail with error: %s", ExceptionUtils.getMessage(e)));
                }
                return oldUri;
            }
        }
    }

}
