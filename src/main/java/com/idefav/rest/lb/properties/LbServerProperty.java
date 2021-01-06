package com.idefav.rest.lb.properties;

import java.util.Map;

/**
 * 负载均衡节点属性配置
 *
 * @author wuzishu
 */
public class LbServerProperty {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
