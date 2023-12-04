package nl.ricoapon.day4;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record ScratchCard(Integer id, List<Integer> winningNumbers, List<Integer> numbers) {

    private final static Pattern ID = Pattern.compile("Card *(\\d+)");

    public static ScratchCard of(String input) {
        Matcher idMatcher = ID.matcher(input.split(":")[0]);
        if (!idMatcher.matches()) {
            throw new RuntimeException("This should not happen");
        }
        Integer id = Integer.valueOf(idMatcher.group(1));

        String allIntegers = input.split(":")[1];
        List<Integer> winningNumbers = parseIntegers(allIntegers.split("\\|")[0]);
        List<Integer> numbers = parseIntegers(allIntegers.split("\\|")[1]);

        return new ScratchCard(id, winningNumbers, numbers);
    }

    private static List<Integer> parseIntegers(String integers) {
        return Arrays.stream(integers.trim().split(" "))
                .filter(s -> !s.isBlank())
                .map(Integer::parseInt)
                .toList();
    }
}
