package com.paxar.qps.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class AlphanumericComparator implements Comparator<String> {
    @Override
    public int compare(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return 0;
        }
        if (s1 == null) {
            return -1;
        }
        if (s2 == null) {
            return 1;
        }
        String[] chunks1 = StringUtils.splitByCharacterType(s1.toLowerCase());
        String[] chunks2 = StringUtils.splitByCharacterType(s2.toLowerCase());
        for (int i = 0; i < chunks1.length && i < chunks2.length; i++) {
            int result = compareChunks(chunks1[i], chunks2[i]);
            if (result != 0) {
                return result;
            }
        }
        if (chunks1.length != chunks2.length) {
            return new Integer(chunks1.length).compareTo(chunks2.length);
        }
        return 0;
    }

    private int compareChunks(String chunk1, String chunk2) {
        if (StringUtils.isNumeric(chunk1) && StringUtils.isNumeric(chunk2)) {
            return Long.valueOf(chunk1).compareTo(Long.valueOf(chunk2));
        } else if (StringUtils.isBlank(chunk1) && StringUtils.isBlank(chunk2)) {
            return 0;
        } else {
            return chunk1.compareTo(chunk2);
        }
    }
}
