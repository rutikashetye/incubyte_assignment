package com.rutika;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AddStringTest {

    private static final String DOUBLE_SLASH = "//";
    private static final String NEW_LINE = "\n";

    @Test
    public void givenValidString_whenAddString_thenReturnSum() {
        assertEquals(0, addString(""));
        assertEquals(1, addString("1"));
        assertEquals(6, addString("1,5"));
        assertEquals(6, addString("1\n2,3"));
        assertEquals(3, addString("//;\n1;2"));
        assertEquals(6, addString("//;\n1;2;;;3"));
        assertEquals(17, addString("//;\n1;2;5//,\n1\n3,5"));
        assertEquals(2, addString("2,1001"));
        assertEquals(6, addString("//[***]\n1***2***3"));
    }

    @Test
    public void givenInValidString_whenAddString_thenThrowException() {
        assertThrows(IllegalArgumentException.class, ()-> addString("//;\n1;-2;5//,\n1\n3,-5"));
    }

    private long addString(String s) {
        if(s.isEmpty()) {
            return 0;
        }

        StringBuilder exp = new StringBuilder();
        exp.append(",\n"); //primary characters to eliminate
        String temp = s;
        while(temp.contains(DOUBLE_SLASH)) {
            int startIndex = temp.indexOf(DOUBLE_SLASH);
            int endIndex = temp.indexOf(NEW_LINE);
            String delimiter = temp.substring(startIndex + 2, endIndex); // +2 is used to remove double slash.
            if (delimiter.contains("[")) {
                exp.append(delimiter.replace("[", "").replace("]", ""));
                s = s.replace("[", "").replace("]", "");
            }
            exp.append(delimiter);
            temp = temp.substring(endIndex + 2);  //+2 length of '\n'
        }

        s = s.replace(DOUBLE_SLASH,""); // double_slash is replaced from the original string
        String[] arr = s.split("["+exp+"]");

        List<String> negativeNumbers = getNegativeNumbers(arr);

        if (negativeNumbers.isEmpty()) {
            return Arrays.stream(arr)
                    .filter(c -> !c.isEmpty())
                    .mapToLong(Long::parseLong)
                    .filter(c -> c < 1000)
                    .sum();
        }

        throw new IllegalArgumentException("Negative numbers found: " + String.join(", ", negativeNumbers));
    }

    private List<String> getNegativeNumbers(String[] arr) {
        return Arrays.stream(arr)
                .filter(c -> !c.isEmpty())
                .map(Long::parseLong)
                .filter(n -> n < 0)
                .map(String::valueOf)
                .collect(Collectors.toList());
    }
}