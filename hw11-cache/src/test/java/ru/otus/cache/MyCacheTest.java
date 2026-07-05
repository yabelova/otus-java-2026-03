package ru.otus.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MyCacheTest {

    private HwCache<String, String> cache;

    @BeforeEach
    void setUp() {
        cache = new MyCache<>();
    }

    @Test
    @DisplayName("should put and get value")
    void putAndGet() {
        cache.put("k", "v");
        assertThat(cache.get("k")).isEqualTo("v");
    }

    @Test
    @DisplayName("should return null for missing key")
    void getReturnsNullForMissingKey() {
        assertThat(cache.get("no")).isNull();
    }

    @Test
    @DisplayName("should remove value")
    void remove() {
        cache.put("k", "v");
        cache.remove("k");
        assertThat(cache.get("k")).isNull();
    }

    @Test
    @DisplayName("should notify listener on put")
    void listenerNotifiedOnPut() {
        var listener = new TestListener();
        cache.addListener(listener);
        cache.put("k", "v");
        assertThat(listener.getEvents()).contains("put");
    }

    @Test
    @DisplayName("should notify listener on get")
    void listenerNotifiedOnGet() {
        cache.put("k", "v");
        var listener = new TestListener();
        cache.addListener(listener);
        cache.get("k");
        assertThat(listener.getEvents()).contains("get");
    }

    @Test
    @DisplayName("should notify listener on remove")
    void listenerNotifiedOnRemove() {
        cache.put("k", "v");
        var listener = new TestListener();
        cache.addListener(listener);
        cache.remove("k");
        assertThat(listener.getEvents()).contains("remove");
    }

    @Test
    @DisplayName("should stop notifying after listener removal")
    void removeListener() {
        var listener = new TestListener();
        cache.addListener(listener);
        cache.removeListener(listener);
        cache.put("k", "v");
        assertThat(listener.getEvents()).isEmpty();
    }

    @Test
    @DisplayName("should overwrite existing key")
    void putOverwritesExistingKey() {
        cache.put("k", "v1");
        cache.put("k", "v2");
        assertThat(cache.get("k")).isEqualTo("v2");
    }

    @Test
    @DisplayName("should clear entries with weakly reachable keys after GC")
    void gcClearsEntriesWithWeakKeys() throws InterruptedException {
        String key = new String("k0");
        cache.put(key, "v0");

        assertThat(cache.get(key)).isEqualTo("v0");

        key = null;
        
        System.gc();
        Thread.sleep(100);

        assertThat(cache.get("k0")).isNull();
    }
}
