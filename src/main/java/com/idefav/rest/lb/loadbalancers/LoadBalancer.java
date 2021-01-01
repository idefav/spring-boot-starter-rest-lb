package com.idefav.rest.lb.loadbalancers;

import com.idefav.rest.lb.LbServer;

/**
 * the Loadbancor description.
 *
 * @author wuzishu
 */
public interface LoadBalancer {
    /**
     * 获取服务
     *
     * @return 服务
     */
    LbServer getServer();
}
