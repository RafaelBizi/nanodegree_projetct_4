package com.example.demo;

import com.example.demo.security.RequestBodyReaderAuthenticationFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

/**
 * @author RafaelBizi
 * @created 18/11/2021 - 16:28
 * @project project_4_udacity
 */
public class TestsInjection {

    public static void injectObjects(Object target, String fieldName, Object toInject) {
        Logger LOGGER = LogManager.getLogger(TestsInjection.class);
        boolean isPrivateClass = false;

        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            if (!f.canAccess(target)) {
                f.setAccessible(true);
                isPrivateClass = true;
            }
            f.set(target, toInject);
            if (isPrivateClass) {
                f.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.info("ERROR -> " + e);
        }
    }

}
