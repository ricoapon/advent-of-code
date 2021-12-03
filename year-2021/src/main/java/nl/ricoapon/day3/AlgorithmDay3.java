package nl.ricoapon.day3;

import nl.ricoapon.framework.Algorithm;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AlgorithmDay3 implements Algorithm {
    @Override
    public String part1(String input) {
        List<String> binaryInput = Arrays.stream(input.split("\n")).toList();
        int length = binaryInput.get(0).length();

        StringBuilder gammaBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            gammaBuilder.append(getMostCommonChar(binaryInput, i));
        }

        int gamma = Integer.parseInt(gammaBuilder.toString(), 2);
        int epsilon = (int) Math.pow(2, length) - 1 - gamma;

        return String.valueOf(gamma * epsilon);
    }

    @Override
    public String part2(String input) {
        List<String> binaryInput = Arrays.stream(input.split("\n")).toList();

        List<String> leftover = binaryInput;
        int index = 0;
        while (leftover.size() > 1) {
            char mostCommonCharAtIndex = getMostCommonChar(leftover, index);

            // Integer in streams must be effectively final. So we copy the value into a final variable.
            final int finalIndex = index;
            leftover = leftover.stream()
                    .filter(s -> s.charAt(finalIndex) == mostCommonCharAtIndex)
                    .toList();

            index++;
        }
        int oxygenGeneratorRating = Integer.parseInt(leftover.get(0), 2);

        leftover = binaryInput;
        index = 0;
        while (leftover.size() > 1) {
            char leastCommonCharAtIndex = revert(getMostCommonChar(leftover, index));

            // Integer in streams must be effectively final. So we copy the value into a final variable.
            final int finalIndex = index;
            leftover = leftover.stream()
                    .filter(s -> s.charAt(finalIndex) == leastCommonCharAtIndex)
                    .toList();

            index++;
        }
        int co2ScrubberRating = Integer.parseInt(leftover.get(0), 2);

        return String.valueOf(oxygenGeneratorRating * co2ScrubberRating);
    }

    private char getMostCommonChar(List<String> items, int index) {
        return items.stream()
                .map(s -> s.charAt(index))
                .collect(Collectors.groupingBy(x -> x, Collectors.counting()))
                .entrySet().stream()
                .max(this::compareByValue)
                .orElseThrow().getKey();
    }

    private int compareByValue(Map.Entry<Character, Long> e1, Map.Entry<Character, Long> e2) {
        if (e1.getValue() > e2.getValue() || e1.getValue() < e2.getValue()) {
            return (int) (e1.getValue() - e2.getValue());
        }
        if (e1.getKey() != e2.getKey()) {
            return e1.getKey() - e2.getKey();
        }
        return 0;
    }

    private char revert(char c) {
        if (c == '0') {
            return '1';
        } else {
            return '0';
        }
    }
}
