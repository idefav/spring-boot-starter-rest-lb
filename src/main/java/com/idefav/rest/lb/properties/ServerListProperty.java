package com.idefav.rest.lb.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * the ServerListProperty description.
 *
 * @author wuzishu
 */
@ConfigurationProperties(prefix = "idefav")
public class ServerListProperty {

    private Map<String, ServiceProperty> serviceList = new HashMap<>();

    /**
     * Gets service list.
     *
     * @return the service list
     */
    public Map<String, ServiceProperty> getServiceList() {
        return serviceList;
    }

    /**
     * Sets service list.
     *
     * @param serviceList the service list
     */
    public void setServiceList(Map<String, ServiceProperty> serviceList) {
        this.serviceList = serviceList;
    }
}
