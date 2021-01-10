package com.idefav.rest.lb.properties;


import com.idefav.rest.lb.loadbalancers.RandomLoadBalaner;

import java.util.List;

/**
 * the ServiceConfig description.
 *
 * @author wuzishu
 */
public class ServiceProperty {

    /**
     * 服务列表
     */
    private List<LbServerProperty> serverList;

    /**
     * 负责均衡器 默认: 随机负载
     */
    private Class<?> loadBalancor = RandomLoadBalaner.class;

    /**
     * 重试属性
     */
    private RetryProperty retryProperty;

    /**
     * Gets server list.
     *
     * @return the server list
     */
    public List<LbServerProperty> getServerList() {
        return serverList;
    }

    /**
     * Sets server list.
     *
     * @param serverList the server list
     */
    public void setServerList(List<LbServerProperty> serverList) {
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
     * Gets retry property.
     *
     * @return the retry property
     */
    public RetryProperty getRetryProperty() {
        return retryProperty;
    }

    /**
     * Sets retry property.
     *
     * @param retryProperty the retry property
     */
    public void setRetryProperty(RetryProperty retryProperty) {
        this.retryProperty = retryProperty;
    }
}
