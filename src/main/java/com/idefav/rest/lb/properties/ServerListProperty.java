package com.idefav.rest.lb.properties;

import com.idefav.rest.lb.loadbalancers.RandomLoadBalaner;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the ServerListProperty description.
 *
 * @author wuzishu
 */
@ConfigurationProperties(prefix = "idefav")
public class ServerListProperty {

    private Map<String, ServiceConfig> serviceList = new HashMap<>();

    /**
     * Gets service list.
     *
     * @return the service list
     */
    public Map<String, ServiceConfig> getServiceList() {
        return serviceList;
    }

    /**
     * Sets service list.
     *
     * @param serviceList the service list
     */
    public void setServiceList(Map<String, ServiceConfig> serviceList) {
        this.serviceList = serviceList;
    }


    /**
     * The type Service config 2.
     */
    static class ServiceConfig2 {
        private List<String> serverList;
        private Class<?> loadBancor = RandomLoadBalaner.class;

        /**
         * Gets server list.
         *
         * @return the server list
         */
        public List<String> getServerList() {
             return serverList;
         }

        /**
         * Sets server list.
         *
         * @param serverList the server list
         */
        public void setServerList(List<String> serverList) {
             this.serverList = serverList;
         }

        /**
         * Gets load bancor.
         *
         * @return the load bancor
         */
        public Class<?> getLoadBancor() {
             return loadBancor;
         }

        /**
         * Sets load bancor.
         *
         * @param loadBancor the load bancor
         */
        public void setLoadBancor(Class<?> loadBancor) {
             this.loadBancor = loadBancor;
         }
     }
}
