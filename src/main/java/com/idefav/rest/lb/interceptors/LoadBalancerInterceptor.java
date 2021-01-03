package com.idefav.rest.lb.interceptors;


import com.idefav.rest.lb.LbServer;
import com.idefav.rest.lb.RestUtil;
import com.idefav.rest.lb.loadbalancers.LoadBalancer;
import com.idefav.rest.lb.loadbalancers.RandomLoadBalaner;
import com.idefav.rest.lb.properties.ServiceConfig;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
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

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        LoadBalancerHttpReqeustWrapper reqeustWrapper = new LoadBalancerHttpReqeustWrapper(httpRequest);
        return clientHttpRequestExecution.execute(reqeustWrapper, bytes);
    }

    /**
     * The type Load balancer http reqeust wrapper.
     */
    public class LoadBalancerHttpReqeustWrapper extends HttpRequestWrapper {

        private LbServer choosedServer = null;

        /**
         * Gets choosed server.
         *
         * @return the choosed server
         */
        public LbServer getChoosedServer() {
            return choosedServer;
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
            String serviceId = oldUri.getHost();
            LoadBalancer loadBancor = getLoadbalancer(serviceId);
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
