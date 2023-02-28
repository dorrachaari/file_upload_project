package com.example.demo.utilities;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.function.Function;

public class StringUtility {

    private StringUtility() {}

    public static char findMostFrequentCharacter(final String s) {
        if(StringUtils.isBlank(s)){
            return Character.MIN_VALUE;
        }
        return s.chars()
                .filter(c -> !Character.isWhitespace(c)) // ignoring space
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(IllegalArgumentException::new);
    }

    public static String reverseString(String str) {
        if (StringUtils.isBlank(str)) {
            return StringUtils.EMPTY;
        }
        return new StringBuilder(str).reverse().toString();
    }
}
