package com.idefav.rest.lb.loadbalancers;


import com.idefav.rest.lb.LbServer;

import java.util.List;
import java.util.Random;

/**
 * the RandomLoadBalancer description.
 *
 * @author wuzishu
 */
public class RandomLoadBalaner extends AbstractLoadBalancer {
    private final Random random = new Random();

    /**
     * Instantiates a new Random load balaner.
     *
     * @param serverList the server list
     */
    public RandomLoadBalaner(List<LbServer> serverList) {
        super(serverList);
    }


    @Override
    public LbServer getServer() {
        checkServerList();
        int i = random.nextInt(1024) % getServerList().size();
        return getServerList().get(i);
    }
}
