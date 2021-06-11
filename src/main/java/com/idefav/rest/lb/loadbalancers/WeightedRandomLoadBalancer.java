package com.idefav.rest.lb.loadbalancers;

import com.idefav.rest.lb.LbServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * 带权重的随机算法
 *
 * @author wuzishu
 */
public class WeightedRandomLoadBalancer extends AbstractLoadBalancer {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeightedRandomLoadBalancer.class);
    private TreeMap<Double, LbServer> weightMap = new TreeMap<>();

    /**
     * Instantiates a new Random with weight load balancer.
     *
     * @param serverList the server list
     */
    public WeightedRandomLoadBalancer(List<LbServer> serverList) {
        super(serverList);
        checkServerList();
        serverList.forEach(k -> {
            double lastWeight = this.weightMap.size() == 0 ? 0 : this.weightMap.lastKey();
            Integer weight = k.getWeight();
            LOGGER.debug("实例列表: {}, 权重: {}", k.getUrl(), k.getWeight());
            if (weight == null) {
                LOGGER.warn("There are services without configured weights,{}", k);
                return;
            }
            if (weight <= 0) {
                return;
            }
            weightMap.put(weight + lastWeight, k);
        });
    }

    @Override
    public LbServer getServer() {
        double randomWeight = weightMap.lastKey() * Math.random();
        NavigableMap<Double, LbServer> doubleLbServerNavigableMap = weightMap.tailMap(randomWeight, false);
        return weightMap.get(doubleLbServerNavigableMap.firstKey());
    }
}
