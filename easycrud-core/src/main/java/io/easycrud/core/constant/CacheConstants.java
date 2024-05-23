package io.easycrud.core.constant;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class CacheConstants {

    static {
        Set<String> values = new HashSet<>();
        for (Field field : CacheConstants.class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
                try {
                    Object value = field.get(null);
                    if (!values.add(value.toString())) {
                        throw new IllegalStateException("Duplicate constant value detected: " + value);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error accessing constant value", e);
                }
            }
        }
    }

    public static void triggerStaticInitialization() {
        log.info("Start cache constants initialization.");
    }

    private static final String CACHE_VERSION = "c_1";

    public static final String EXAMPLE_CACHE_KEY = CACHE_VERSION + "exa";
}
