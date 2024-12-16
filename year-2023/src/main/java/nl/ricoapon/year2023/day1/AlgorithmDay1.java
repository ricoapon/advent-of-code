package nl.ricoapon.year2023.day1;

import nl.ricoapon.framework.Algorithm;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Comparator;

public class AlgorithmDay1 implements Algorithm {
    @Override
    public Object part1(String input) {
        return Arrays.stream(input.split("\\r?\\n"))
                .mapToInt(this::determineIntFromLine)
                .sum();
    }

    private Integer determineIntFromLine(String line) {
        String first = null;
        String last = null;
        for (String s : line.split("")) {
            if (!isInteger(s)) {
                continue;
            }

            if (first == null) {
                first = s;
            }
            last = s;
        }

        return Integer.valueOf(first + last);
    }

    private boolean isInteger(String s) {
        return s.matches("-?\\d+");
    }

    @Override
    public Object part2(String input) {
        return Arrays.stream(input.split("\\r?\\n"))
                .mapToInt(this::determineIntFromLinePart2)
                .sum();
    }

    private Integer determineIntFromLinePart2(String line) {
        Map<String, Integer> ints = Map.of("one", 1, "two", 2, "three", 3, "four", 4, "five", 5, "six", 6, "seven", 7, "eight", 8, "nine", 9);
        List<String> allIntegers = List.of("one", "1", "two", "2", "three", "3", "four", "4", "five", "5", "six", "6",
                "seven", "7", "eight", "8", "nine", "9");
        
        String first = allIntegers.stream()
                .filter(line::contains).min(Comparator.comparingInt(line::indexOf)).orElseThrow();
        String last = allIntegers.stream()
                .filter(line::contains).max(Comparator.comparingInt(line::lastIndexOf)).orElseThrow();
        
        String firstAsIntString = first;
        if (ints.containsKey(first)) {
            firstAsIntString = String.valueOf(ints.get(first));
        }
        String lastAsIntString = last;
        if (ints.containsKey(last)) {
            lastAsIntString = String.valueOf(ints.get(last));
        }

        return Integer.valueOf(firstAsIntString + lastAsIntString);
    }

}
