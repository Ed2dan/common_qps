package com.paxar.qps.common.utils.map;

import java.util.LinkedHashMap;

public class LinkedMapUtils {

    private LinkedMapUtils() {
    }

    /**
     * Use the method for compact map initialization of LinkedHashMap e.g.:
     * <pre>
     *     Map<String, String> parameters = MapUtils
     *         .addEntry(WebUtils.REQ_ATTR_ORDER_ID, String.valueOf(orderId))
     *         .addEntry(WebUtils.REQ_ATTR_ERROR_MESSAGE, FAILED_TO_SHIP_ITEMS_UNEXPECTED_ERROR_OCCURRED);
     * </pre>
     */
    public static <K, V> LinkedMapBuilder<K, V> addEntry(K key, V value) {
        return new LinkedMapBuilder<K, V>().addEntry(key, value);
    }

    public static class LinkedMapBuilder<K, V> extends LinkedHashMap<K, V> {

        private static final long serialVersionUID = -4591423956973278947L;

        public LinkedMapBuilder<K, V> addEntry(K key, V value) {
            this.put(key, value);
            return this;
        }
    }
}
