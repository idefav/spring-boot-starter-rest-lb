package com.idefav.rest.lb;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

/**
 * The interface Load balanced.
 *
 * @author wuzishu
 */
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Qualifier
public @interface LoadBalanced {
}
