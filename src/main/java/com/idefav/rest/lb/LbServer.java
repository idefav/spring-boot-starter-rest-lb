package com.idefav.rest.lb;

import java.util.Map;

/**
 * 负载均衡的服务器
 *
 * @author wuzishu
 */
public class LbServer {

    /**
     * 服务地址 , 如: localhost:8080
     */
    private String url;

    /**
     * 附加属性
     */
    private Map<String, String> properties;

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets properties.
     *
     * @return the properties
     */
    public Map<String, String> getProperties() {
        return properties;
    }

    /**
     * Sets properties.
     *
     * @param properties the properties
     */
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
