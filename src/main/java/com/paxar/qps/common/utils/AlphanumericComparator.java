package com.paxar.qps.common.utils;

import java.math.BigInteger;
import java.util.Comparator;
import org.apache.commons.lang3.StringUtils;

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
            return Integer.compare(chunks1.length, chunks2.length);
        }
        return 0;
    }

    private int compareChunks(String chunk1, String chunk2) {
        if (StringUtils.isNumeric(chunk1) && StringUtils.isNumeric(chunk2)) {
            return new BigInteger(chunk1).compareTo(new BigInteger(chunk2));
        } else if (StringUtils.isBlank(chunk1) && StringUtils.isBlank(chunk2)) {
            return 0;
        } else {
            return chunk1.compareTo(chunk2);
        }
    }
}
