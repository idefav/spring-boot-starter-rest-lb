package com.idefav.rest.lb.loadbalancers;

import com.idefav.rest.lb.LbServer;

import java.util.List;

/**
 * Abstract LoadBalaner
 *
 * @author wuzishu
 */
public abstract class AbstractLoadBalancer implements LoadBalancer {

    /**
     * Instantiates a new Abstract load balancer.
     *
     * @param serverList the server list
     */
    public AbstractLoadBalancer(List<LbServer> serverList) {
        this.serverList = serverList;
    }

    private final List<LbServer> serverList;

    /**
     * Gets server list.
     *
     * @return the server list
     */
    public List<LbServer> getServerList() {
        return serverList;
    }

    /**
     * check server node list
     *
     * @return boolean boolean
     */
    protected boolean checkServerList() {
        if (serverList == null || serverList.size() <= 0) {
            throw new RuntimeException("No list of available services");
        }
        return true;
    }

    @Override
    public abstract LbServer getServer();
}
