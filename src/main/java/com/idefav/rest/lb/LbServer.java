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
     * 权重, 只有在权重随机算法中有效
     */
    private Integer weight;

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

    /**
     * Gets weight.
     *
     * @return the weight
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * Sets weight.
     *
     * @param weight the weight
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
