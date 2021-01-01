package com.idefav.rest.lb.loadbalancers;

import com.idefav.rest.lb.LbServer;

import java.util.List;

/**
 * Abstract LoadBanlener
 *
 * @author wuzishu
 */
public abstract class AbstractLoadBalancer implements LoadBalancer {

    public AbstractLoadBalancer(List<LbServer> serverList) {
        this.serverList = serverList;
    }

    private List<LbServer> serverList;

    public List<LbServer> getServerList() {
        return serverList;
    }

    /**
     * check server node list
     *
     * @return boolean
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
