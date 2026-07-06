/*
 * Source: Java-Pro.zip (Otus Java Pro course materials, lesson L22-cache)
 */
package ru.otus.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HwCacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(HwCacheDemo.class);

    public static void main(String[] args) {
        new HwCacheDemo().demo();
    }

    private void demo() {
        HwCache<String, Integer> cache = new MyCache<>();

        // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
        @SuppressWarnings("java:S1604")
        HwListener<String, Integer> listener = new HwListener<String, Integer>() {
            @Override
            public void notify(String key, Integer value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };

        cache.addListener(listener);
        cache.put("1", 1);

        logger.info("getValue:{}", cache.get("1"));
        cache.remove("1");
        cache.removeListener(listener);
    }
}
