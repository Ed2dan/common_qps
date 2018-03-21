package com.paxar.qps.common.utils.map;

import java.util.HashMap;
import java.util.Map;

public final class MapUtils {

    private MapUtils() {
    }

    /**
     * Use the method for compact map initialization e.g.:
     * <pre>
     *     Map<String, String> parameters = MapUtils
     *         .addEntry(WebUtils.REQ_ATTR_ORDER_ID, String.valueOf(orderId))
     *         .addEntry(WebUtils.REQ_ATTR_ERROR_MESSAGE, FAILED_TO_SHIP_ITEMS_UNEXPECTED_ERROR_OCCURRED);
     * </pre>
     */
    public static <K, V> MapBuilder<K, V> addEntry(K key, V value) {
        return new MapBuilder<K, V>().addEntry(key, value);
    }

    public static <K, V> V getMapValueOrThrowException(Map<K, V> map, K key) {
        V value = map.get(key);
        if (value == null) {
            throw new IllegalArgumentException(String.format("Value not found for key %s in map %n%s", key, map));
        }
        return value;
    }

    public static class MapBuilder<K, V> extends HashMap<K, V> {

        private static final long serialVersionUID = -4591423956973278947L;

        public MapBuilder<K, V> addEntry(K key, V value) {
            this.put(key, value);
            return this;
        }
    }
}
