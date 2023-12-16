package nl.ricoapon.year2021.day8;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay8 implements Algorithm {
    @Override
    public String part1(String input) {
        long count = Arrays.stream(input.split("\n"))
                .map(s -> s.split("\\|")[1])
                .flatMap(s -> Arrays.stream(s.split(" ")))
                // Filter 1, 4, 7 and 8
                .filter(s -> s.length() == 2 || s.length() == 3 || s.length() == 4 || s.length() == 7)
                .count();

        return String.valueOf(count);
    }

    @Override
    public String part2(String input) {
        List<String> lines = Arrays.stream(input.split("\n")).toList();
        return String.valueOf(lines.stream().mapToInt(this::calculateNumber).sum());
    }

    private int calculateNumber(String line) {
        List<String> section = Arrays.stream(line.split("\\|")).toList();
        List<Digit> firstDigits = Arrays.stream(section.get(0).split(" ")).filter(s -> s.length() > 0).map(Digit::of).toList();
        Map<Digit, Integer> translationMap = determineTranslationMap(firstDigits);

        List<Digit> finalDigits = Arrays.stream(section.get(1).split(" ")).filter(s -> s.length() > 0).map(Digit::of).toList();
        List<Integer> finalNumber = finalDigits.stream().map(translationMap::get).toList();
        return finalNumber.get(0) * 1000 + finalNumber.get(1) * 100 + finalNumber.get(2) * 10 + finalNumber.get(3);
    }

    private Map<Digit, Integer> determineTranslationMap(List<Digit> digits) {
        Map<Digit, Integer> translationMap = new HashMap<>();

        Map<Integer, List<Digit>> sizeMap = digits.stream()
                .collect(Collectors.groupingBy(digit -> digit.getSegments().size(), Collectors.toList()));

        // Determine the easy ones.
        Digit one = sizeMap.get(2).get(0);
        translationMap.put(one, 1);
        translationMap.put(sizeMap.get(3).get(0), 7);
        Digit four = sizeMap.get(4).get(0);
        translationMap.put(four, 4);
        translationMap.put(sizeMap.get(7).get(0), 8);

        // Determine 2, 3, 5 using intersection tricks.
        Digit two = sizeMap.get(5).stream().filter(d -> d.intersection(four).size() == 2).findAny().orElseThrow();
        translationMap.put(two, 2);
        Digit five = sizeMap.get(5).stream().filter(d -> d.intersection(two).size() == 3).findAny().orElseThrow();
        Digit three = sizeMap.get(5).stream().filter(d -> d.intersection(two).size() == 4).findAny().orElseThrow();
        translationMap.put(five, 5);
        translationMap.put(three, 3);

        // Determine 0, 6, 9 using intersection tricks.
        Digit six = sizeMap.get(6).stream().filter(d -> d.intersection(one).size() == 1).findAny().orElseThrow();
        translationMap.put(six, 6);
        Digit zero = sizeMap.get(6).stream()
                .filter(d -> d != six)
                .filter(d -> d.intersection(four).size() == 3).findAny().orElseThrow();
        translationMap.put(zero, 0);
        Digit nine = sizeMap.get(6).stream().filter(d -> d != six && d != zero).findAny().orElseThrow();
        translationMap.put(nine, 9);
        return translationMap;
    }
}
