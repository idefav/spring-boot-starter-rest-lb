package com.idefav.rest.lb.properties;

import com.idefav.rest.lb.recovery.AbstractRecoveryCallback;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.retry.RecoveryCallback;

/**
 * 重试设置
 *
 * @author wuzishu
 */
public class RetryProperty {

    /**
     * 是否启用
     */
    private boolean enable;

    /**
     * 重试设置
     */
    private Retry retry;

    /**
     * 回退策略
     */
    private BackOff backOff;

    /**
     * 出发重试的条件
     */
    private RetryCondition condition;

    public RetryCondition getCondition() {
        return condition;
    }

    public void setCondition(RetryCondition condition) {
        this.condition = condition;
    }

    /**
     * 重试失败, 降级回调
     */
    private Class<? extends AbstractRecoveryCallback> recoveryCallback;

    /**
     * Gets recovery callback.
     *
     * @return the recovery callback
     */
    public Class<? extends AbstractRecoveryCallback> getRecoveryCallback() {
        return recoveryCallback;
    }

    /**
     * Sets recovery callback.
     *
     * @param recoveryCallback the recovery callback
     */
    public void setRecoveryCallback(Class<? extends AbstractRecoveryCallback> recoveryCallback) {
        this.recoveryCallback = recoveryCallback;
    }

    /**
     * Gets back off.
     *
     * @return the back off
     */
    public BackOff getBackOff() {
        return backOff;
    }

    /**
     * Sets back off.
     *
     * @param backOff the back off
     */
    public void setBackOff(BackOff backOff) {
        this.backOff = backOff;
    }

    /**
     * Gets retry.
     *
     * @return the retry
     */
    public Retry getRetry() {
        return retry;
    }

    /**
     * Sets retry.
     *
     * @param retry the retry
     */
    public void setRetry(Retry retry) {
        this.retry = retry;
    }

    /**
     * Is enable boolean.
     *
     * @return the boolean
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * Sets enable.
     *
     * @param enable the enable
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * 重试设置
     */
    public static class Retry {
        /**
         * 最大重试次数
         */
        private Integer maxAttempts;

        /**
         * 超时
         */
        private Long timeout;

        /**
         * 熔断器开启超时时间
         */
        private Long openTimeout;

        /**
         * 熔断器闭合超时时间
         */
        private Long resetTimeout;

        /**
         * Gets max attempts.
         *
         * @return the max attempts
         */
        public Integer getMaxAttempts() {
            return maxAttempts;
        }

        /**
         * Sets max attempts.
         *
         * @param maxAttempts the max attempts
         */
        public void setMaxAttempts(Integer maxAttempts) {
            this.maxAttempts = maxAttempts;
        }

        /**
         * Gets timeout.
         *
         * @return the timeout
         */
        public Long getTimeout() {
            return timeout;
        }

        /**
         * Sets timeout.
         *
         * @param timeout the timeout
         */
        public void setTimeout(Long timeout) {
            this.timeout = timeout;
        }

        /**
         * Gets open timeout.
         *
         * @return the open timeout
         */
        public Long getOpenTimeout() {
            return openTimeout;
        }

        /**
         * Sets open timeout.
         *
         * @param openTimeout the open timeout
         */
        public void setOpenTimeout(Long openTimeout) {
            this.openTimeout = openTimeout;
        }

        /**
         * Gets reset timeout.
         *
         * @return the reset timeout
         */
        public Long getResetTimeout() {
            return resetTimeout;
        }

        /**
         * Sets reset timeout.
         *
         * @param resetTimeout the reset timeout
         */
        public void setResetTimeout(Long resetTimeout) {
            this.resetTimeout = resetTimeout;
        }
    }

    /**
     * 重试回退策略
     */
    public static class BackOff {

        /**
         * 回退
         */
        private BackOffEnum backOff;

        /**
         * 基础回避重试时间
         */
        private Long backOffPeriod;

        /**
         * 最大回避重试时间
         */
        private Long maxBackOffPeriod;

        /**
         * 初始回避间隔
         */
        private Long initialInterval;

        /**
         * 最大回避间隔
         */
        private Long maxInterval;

        /**
         * 间隔增加间隔
         */
        private Double multiplier;

        /**
         * Gets back off.
         *
         * @return the back off
         */
        public BackOffEnum getBackOff() {
            return backOff;
        }

        /**
         * Sets back off.
         *
         * @param backOff the back off
         */
        public void setBackOff(BackOffEnum backOff) {
            this.backOff = backOff;
        }

        /**
         * Gets back off period.
         *
         * @return the back off period
         */
        public Long getBackOffPeriod() {
            return backOffPeriod;
        }

        /**
         * Sets back off period.
         *
         * @param backOffPeriod the back off period
         */
        public void setBackOffPeriod(Long backOffPeriod) {
            this.backOffPeriod = backOffPeriod;
        }

        /**
         * Gets max back off period.
         *
         * @return the max back off period
         */
        public Long getMaxBackOffPeriod() {
            return maxBackOffPeriod;
        }

        /**
         * Sets max back off period.
         *
         * @param maxBackOffPeriod the max back off period
         */
        public void setMaxBackOffPeriod(Long maxBackOffPeriod) {
            this.maxBackOffPeriod = maxBackOffPeriod;
        }

        /**
         * Gets initial interval.
         *
         * @return the initial interval
         */
        public Long getInitialInterval() {
            return initialInterval;
        }

        /**
         * Sets initial interval.
         *
         * @param initialInterval the initial interval
         */
        public void setInitialInterval(Long initialInterval) {
            this.initialInterval = initialInterval;
        }

        /**
         * Gets max interval.
         *
         * @return the max interval
         */
        public Long getMaxInterval() {
            return maxInterval;
        }

        /**
         * Sets max interval.
         *
         * @param maxInterval the max interval
         */
        public void setMaxInterval(Long maxInterval) {
            this.maxInterval = maxInterval;
        }

        /**
         * Gets multiplier.
         *
         * @return the multiplier
         */
        public Double getMultiplier() {
            return multiplier;
        }

        /**
         * Sets multiplier.
         *
         * @param multiplier the multiplier
         */
        public void setMultiplier(Double multiplier) {
            this.multiplier = multiplier;
        }
    }

    /**
     * 触发重试的条件
     */
    public static class RetryCondition {

        /**
         * 需要重试的状态码
         */
        private HttpStatus[] needRetryStatusCodes = new HttpStatus[]{HttpStatus.INTERNAL_SERVER_ERROR};

        /**
         * 需要重试的方法
         */
        private HttpMethod[] needRetryHttpMethods = new HttpMethod[]{HttpMethod.GET};

        public HttpStatus[] getNeedRetryStatusCodes() {
            return needRetryStatusCodes;
        }

        public void setNeedRetryStatusCodes(HttpStatus[] needRetryStatusCodes) {
            this.needRetryStatusCodes = needRetryStatusCodes;
        }

        public HttpMethod[] getNeedRetryHttpMethods() {
            return needRetryHttpMethods;
        }

        public void setNeedRetryHttpMethods(HttpMethod[] needRetryHttpMethods) {
            this.needRetryHttpMethods = needRetryHttpMethods;
        }
    }

    /**
     * The enum Back off enum.
     */
    public enum BackOffEnum {
        /**
         * 不回避
         */
        NO_BACKOFF(0, "不回避"),

        /**
         * 固定回避策略
         */
        FIXED_BACKOFF(1, "固定回避策略"),

        /**
         * 随机时间回避策略
         */
        UNIFORM_RANDOM_BACKOFF(2, "随机回避策略"),

        /**
         * 指数回避策略
         */
        EXPONENTIAL_BACKOFF(3, "指数回避策略"),

        /**
         * 指数随机回避策略
         */
        EXPONENTIAL_RANDOM_BACKOFF(4, "指数随机回避策略");
        private int value;
        private String name;

        BackOffEnum(int value, String name) {
            this.value = value;
            this.name = name;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }

        /**
         * Gets name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }
    }
}
