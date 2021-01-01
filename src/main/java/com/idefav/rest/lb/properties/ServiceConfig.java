package com.idefav.rest.lb.properties;


import com.idefav.rest.lb.LbServer;
import com.idefav.rest.lb.loadbalancers.RandomLoadBalaner;

import java.util.List;

/**
 * the ServiceConfig description.
 *
 * @author ${USER}
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
}
