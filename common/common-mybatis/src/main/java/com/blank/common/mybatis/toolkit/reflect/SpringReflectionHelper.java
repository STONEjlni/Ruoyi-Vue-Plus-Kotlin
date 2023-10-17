package com.blank.common.mybatis.toolkit.reflect;

import org.springframework.core.GenericTypeResolver;

/**
 * Spring 反射辅助类
 */
public class SpringReflectionHelper {
    public static Class<?>[] resolveTypeArguments(Class<?> clazz, Class<?> genericIfc) {
        return GenericTypeResolver.resolveTypeArguments(clazz, genericIfc);
    }
}
