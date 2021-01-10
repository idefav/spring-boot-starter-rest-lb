package com.idefav.rest.lb.recovery;

import com.idefav.rest.lb.ServiceConfig;
import com.idefav.rest.lb.interceptors.LoadBalancerInterceptor;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryContext;

import java.util.Map;

import static com.idefav.rest.lb.interceptors.LoadBalancerInterceptor.*;

/**
 * 重试失败, 降级回调
 *
 * @author wuzishu
 */
public abstract class AbstractRecoveryCallback implements RecoveryCallback<ClientHttpResponse> {

    private LoadBalancerInterceptor.LoadBalancerHttpReqeustWrapper reqeustWrapper;
    private ServiceConfig serviceConfig;
    private byte[] body;
    private ClientHttpRequestExecution clientHttpRequestExecution;
    private String serviceId;
    private Map<String, ServiceConfig> serviceConfigMap;
    private ClientHttpResponse response;

    public AbstractRecoveryCallback() {
    }

    /**
     * Gets reqeust wrapper.
     *
     * @return the reqeust wrapper
     */
    public LoadBalancerInterceptor.LoadBalancerHttpReqeustWrapper getReqeustWrapper() {
        return reqeustWrapper;
    }

    /**
     * Gets service config.
     *
     * @return the service config
     */
    public ServiceConfig getServiceConfig() {
        return serviceConfig;
    }

    /**
     * Get body byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * Gets client http request execution.
     *
     * @return the client http request execution
     */
    public ClientHttpRequestExecution getClientHttpRequestExecution() {
        return clientHttpRequestExecution;
    }

    /**
     * Gets service id.
     *
     * @return the service id
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * Gets service config map.
     *
     * @return the service config map
     */
    public Map<String, ServiceConfig> getServiceConfigMap() {
        return serviceConfigMap;
    }

    @Override
    public ClientHttpResponse recover(RetryContext context) throws Exception {
        this.reqeustWrapper = (LoadBalancerInterceptor.LoadBalancerHttpReqeustWrapper) context.getAttribute(LoadBalancerInterceptor.RETRY_CONTEXT_ATTRIBUTE_REQUEST);
        this.body = (byte[]) context.getAttribute(LoadBalancerInterceptor.RETRY_CONTEXT_ATTRIBUTE_BODY);
        this.clientHttpRequestExecution = (ClientHttpRequestExecution) context.getAttribute(LoadBalancerInterceptor.RETRY_CONTEXT_ATTRIBUTE_EXECUTION);
        this.serviceId = (String) context.getAttribute(RETRY_CONTEXT_ATTRIBUTE_SERVICEID);
        this.serviceConfigMap = (Map<String, ServiceConfig>) context.getAttribute(RETRY_CONTEXT_ATTRIBUTE_SERVICELIST);
        this.serviceConfig = (ServiceConfig) context.getAttribute(RETRY_CONTEXT_ATTRIBUTE_SERVICECONFIG);
        this.response = (ClientHttpResponse) context.getAttribute(RETRY_CONTEXT_ATTRIBUTE_RESPONSE);
        return invoke(context, reqeustWrapper, response);
    }

    /**
     * Invoke client http response.
     *
     * @param context the context
     * @return the client http response
     */
    protected abstract ClientHttpResponse invoke(RetryContext context, LoadBalancerInterceptor.LoadBalancerHttpReqeustWrapper httpReqeustWrapper, ClientHttpResponse response);
}
