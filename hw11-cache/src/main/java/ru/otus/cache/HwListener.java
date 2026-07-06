/*
 * Source: Java-Pro.zip (Otus Java Pro course materials, lesson L22-cache)
 */
package ru.otus.cache;

public interface HwListener<K, V> {
    void notify(K key, V value, String action);
}
