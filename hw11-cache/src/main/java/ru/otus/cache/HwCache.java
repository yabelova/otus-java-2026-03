/*
 * Source: Java-Pro.zip (Otus Java Pro course materials, lesson L22-cache)
 */
package ru.otus.cache;

public interface HwCache<K, V> {

    void put(K key, V value);

    void remove(K key);

    V get(K key);

    void addListener(HwListener<K, V> listener);

    void removeListener(HwListener<K, V> listener);
}
