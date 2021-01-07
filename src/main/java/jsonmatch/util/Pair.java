package jsonmatch.util;

import java.util.Map;

public class Pair<K,V> implements Map.Entry<K,V>{
    public static <K,V> Pair<K,V> pair(K key, V value) {
        return new Pair(key, value);
    }
    final K key;
    final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        throw new RuntimeException("This is an immutable pair!");
    }
}
