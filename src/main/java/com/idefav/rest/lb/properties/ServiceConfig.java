package com.idefav.rest.lb.properties;


import com.idefav.rest.lb.LbServer;
import com.idefav.rest.lb.loadbalancers.RandomLoadBalaner;

import java.util.List;

/**
 * the ServiceConfig description.
 *
 * @author wuzishu
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
}
