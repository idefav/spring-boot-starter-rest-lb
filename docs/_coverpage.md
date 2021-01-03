

# Spring Boot RestTemplate With LoadBalancer
[![Build Status](https://travis-ci.org/idefav/spring-boot-starter-rest-lb.svg?branch=master)](https://travis-ci.org/idefav/spring-boot-starter-rest-lb)
[![Coverity Scan Build Status](https://scan.coverity.com/projects/22379/badge.svg)](https://scan.coverity.com/projects/idefav-spring-boot-starter-rest-lb)
[![GitHub release](https://img.shields.io/github/release/idefav/spring-boot-starter-rest-lb.svg)](https://github.com/idefav/spring-boot-starter-rest-lb/releases)
[![Maven Central Repo](https://img.shields.io/maven-central/v/com.idefav/spring-boot-starter-rest-lb.svg)](https://mvnrepository.com/artifact/com.idefav/spring-boot-starter-rest-lb)
![](https://img.shields.io/badge/JAVA-1.8+-green.svg)
![](https://img.shields.io/badge/MAVEN-3.5+-pink.svg)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

> 基于 RestTemplate 的负载均衡实现

- 基于 `Spring Boot` 的 `Starter` 启动器, 开箱即用
- 依赖包较少
- 配置简单, 增强 `RestTemplate` 实现负载均衡
- 扩展简单, 默认实现有 轮询 和 随机 负载规则算法, 支持自定义负载规则
- 使用 Spring Boot 原生配置, 支持 `Spring Cloud Config/Apollo/Nacos` 等配置中心

[GitHub](https://github.com/idefav/spring-boot-starter-rest-lb.git)
[Get Started](#resttemplate-的负载均衡实现)