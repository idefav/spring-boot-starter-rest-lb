[![Build Status](https://travis-ci.org/idefav/spring-boot-starter-rest-lb.svg?branch=master)](https://travis-ci.org/idefav/spring-boot-starter-rest-lb)
[![Coverity Scan Build Status](https://scan.coverity.com/projects/22379/badge.svg)](https://scan.coverity.com/projects/idefav-spring-boot-starter-rest-lb)
[![GitHub release](https://img.shields.io/github/release/idefav/spring-boot-starter-rest-lb.svg)](https://github.com/idefav/spring-boot-starter-rest-lb/releases)
[![Maven Central Repo](https://img.shields.io/maven-central/v/com.idefav/spring-boot-starter-rest-lb.svg)](https://mvnrepository.com/artifact/com.idefav.rest.lb/spring-boot-starter-rest-lb)
![](https://img.shields.io/badge/JAVA-1.8+-green.svg)
![](https://img.shields.io/badge/MAVEN-3.5+-pink.svg)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
# RestTemplate 的负载均衡实现
## 优点
1. 基于 `Spring Boot` 的 `Starter` 启动器, 开箱即用
2. 依赖包较少
3. 配置简单, 增强 `RestTemplate` 实现负载均衡
4. 扩展简单, 默认实现有 轮询 、 随机以及权重随机等 负载规则算法, 支持自定义负载规则
5. 使用 Spring Boot 原生配置, 支持 `Spring Cloud Config/Apollo/Nacos` 等配置中心
## 适用场景
1. 不想使用 `Spring Cloud`、`Dubbo` 等, 大型微服务框架
2. 不想自己维护 Nginx (或者没有维护 Nginx 的运营人员)
3. 不想引入大量依赖, 导致项目依赖复杂
## 使用说明
1.引入依赖
```xml
        <dependency>
            <groupId>com.idefav</groupId>
            <artifactId>spring-boot-starter-rest-lb</artifactId>
            <version>1.0.1</version>
        </dependency>
```
2. 配置 RestTemplate
```java
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
```
3. 使用
```java
    public String accessBaidu() {
        String result = restTemplate.getForObject("https://baidu/", String.class);
        log.info(result);
        return result;
    }
```
注意: baidu 是服务名称, 需要和配置文件里面的服务名称保持一致

4. 配置服务
```properties
idefav.service-list.baidu.load-balancor=com.idefav.rest.lb.loadbalancers.RoundRobinLoadBalancer
idefav.service-list.baidu.server-list[0].url=www.baidu.com
idefav.service-list.baidu.server-list[1].url=www.jd.com
```
5. 启动项目

## 扩展
使用字段 properties 新增 weight 权重字段
```properties
idefav.service-list.baidu.load-balancor=com.idefav.springbootdemo.loadbalancers.RandomWithWeightLoadBalancer
idefav.service-list.baidu.server-list[0].url=www.baidu.com
idefav.service-list.baidu.server-list[0].properties.weight=20
idefav.service-list.baidu.server-list[1].url=www.jd.com
idefav.service-list.baidu.server-list[1].properties.weight=80
```
实现权重随机算法
```java
public class RandomWithWeightLoadBalancer extends AbstractLoadBalancer {

    private TreeMap<Double, LbServer> weightMap = new TreeMap<>();

    public RandomWithWeightLoadBalancer(List<LbServer> serverList) {
        super(serverList);
        checkServerList();
        serverList.forEach(k -> {
            double lastWeight = this.weightMap.size() == 0 ? 0 : this.weightMap.lastKey();
            if (k.getProperties() == null) {
                return;
            }
            String weight = k.getProperties().get("weight");
            if (StringUtils.isEmpty(weight)) {
                return;
            }
            if (!StringUtils.isNumeric(weight)) {
                return;
            }
            weightMap.put(Double.parseDouble(weight) + lastWeight, k);
        });
    }

    @Override
    public LbServer getServer() {
        double randomWeight = weightMap.lastKey() * Math.random();
        NavigableMap<Double, LbServer> doubleLbServerNavigableMap = weightMap.tailMap(randomWeight, false);
        return weightMap.get(doubleLbServerNavigableMap.firstKey());
    }
}
```

