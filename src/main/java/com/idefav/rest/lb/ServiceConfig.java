package com.idefav.rest.lb;

import com.idefav.rest.lb.loadbalancers.RandomLoadBalaner;
import com.idefav.rest.lb.properties.RetryProperty;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.support.RetryTemplate;

import java.util.List;

/**
 * The type Service config.
 */
public class ServiceConfig {
    /**
     * 服务列表
     */
    private List<LbServer> serverList;

    /**
     * 负责均衡器 默认: 随机负载
     */
    private Class<?> loadBalancor = RandomLoadBalaner.class;

    /**
     * 重试设置
     */
    private RetryTemplate retryTemplate;

    /**
     * 触发重试的条件
     */
    private RetryProperty.RetryCondition condition;

    public RetryProperty.RetryCondition getCondition() {
        return condition;
    }

    public void setCondition(RetryProperty.RetryCondition condition) {
        this.condition = condition;
    }

    /**
     * 重试保底回调
     */
    private RecoveryCallback<ClientHttpResponse> recoveryCallback;

    /**
     * Gets recovery callback.
     *
     * @return the recovery callback
     */
    public RecoveryCallback<ClientHttpResponse> getRecoveryCallback() {
        return recoveryCallback;
    }

    /**
     * Sets recovery callback.
     *
     * @param recoveryCallback the recovery callback
     */
    public void setRecoveryCallback(RecoveryCallback<ClientHttpResponse> recoveryCallback) {
        this.recoveryCallback = recoveryCallback;
    }

    /**
     * Gets server list.
     *
     * @return the server list
     */
    public List<LbServer> getServerList() {
        return serverList;
    }

    /**
     * Sets server list.
     *
     * @param serverList the server list
     */
    public void setServerList(List<LbServer> serverList) {
        this.serverList = serverList;
    }

    /**
     * Gets load balancor.
     *
     * @return the load balancor
     */
    public Class<?> getLoadBalancor() {
        return loadBalancor;
    }

    /**
     * Sets load balancor.
     *
     * @param loadBalancor the load balancor
     */
    public void setLoadBalancor(Class<?> loadBalancor) {
        this.loadBalancor = loadBalancor;
    }

    /**
     * Gets retry template.
     *
     * @return the retry template
     */
    public RetryTemplate getRetryTemplate() {
        return retryTemplate;
    }

    /**
     * Sets retry template.
     *
     * @param retryTemplate the retry template
     */
    public void setRetryTemplate(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }
}
