package com.idefav.rest.lb.loadbalancers;


import com.idefav.rest.lb.LbServer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * the RoundRobin description.
 *
 * @author wuzishu
 */
public class RoundRobinLoadBalancer extends AbstractLoadBalancer {

    private AtomicInteger index = new AtomicInteger(0);

    public RoundRobinLoadBalancer(List<LbServer> serverList) {
        super(serverList);
    }


    @Override
    public LbServer getServer() {
        checkServerList();
        int size = getServerList().size();
        int i = this.index.get() % size;
        this.index.compareAndSet(index.get(), i + 1);
        return getServerList().get(i);
    }
}
