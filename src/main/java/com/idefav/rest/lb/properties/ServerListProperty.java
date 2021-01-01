package com.idefav.rest.lb.properties;

import com.idefav.rest.lb.loadbalancers.RandomLoadBalaner;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the ServerListProperty description.
 *
 * @author ${USER}
 */
@ConfigurationProperties(prefix = "idefav")
public class ServerListProperty {

    private Map<String, ServiceConfig> serviceList = new HashMap<>();

    public Map<String, ServiceConfig> getServiceList() {
        return serviceList;
    }

    public void setServiceList(Map<String, ServiceConfig> serviceList) {
        this.serviceList = serviceList;
    }


    static class ServiceConfig2 {
        private List<String> serverList;
        private Class<?> loadBancor = RandomLoadBalaner.class;

         public List<String> getServerList() {
             return serverList;
         }

         public void setServerList(List<String> serverList) {
             this.serverList = serverList;
         }

         public Class<?> getLoadBancor() {
             return loadBancor;
         }

         public void setLoadBancor(Class<?> loadBancor) {
             this.loadBancor = loadBancor;
         }
     }
}
