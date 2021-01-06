package com.idefav.rest.lb;

import com.idefav.rest.lb.loadbalancers.RandomLoadBalaner;
import org.springframework.retry.support.RetryTemplate;

import java.util.List;

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

    public List<LbServer> getServerList() {
        return serverList;
    }

    public void setServerList(List<LbServer> serverList) {
        this.serverList = serverList;
    }

    public Class<?> getLoadBalancor() {
        return loadBalancor;
    }

    public void setLoadBalancor(Class<?> loadBalancor) {
        this.loadBalancor = loadBalancor;
    }

    public RetryTemplate getRetryTemplate() {
        return retryTemplate;
    }

    public void setRetryTemplate(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }
}
