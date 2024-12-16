package nl.ricoapon.year2024.day1;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay1 implements Algorithm {

    private Stream<Integer> determineLeftInput(String input) {
        return Stream.of(input.split("\\r?\\n")).map(s -> s.substring(0, s.indexOf(" "))).map(Integer::parseInt);
    }

    private Stream<Integer> determineRightInput(String input) {
        return Stream.of(input.split("\\r?\\n")).map(s -> s.substring(s.lastIndexOf(" ") + 1)).map(Integer::parseInt);
    }

    @Override
    public Object part1(String input) {
        List<Integer> leftSorted = determineLeftInput(input).sorted().toList();
        List<Integer> rightSorted = determineRightInput(input).sorted().toList();

        Integer sumOfDifferences = 0;

        for (int i = 0; i < leftSorted.size(); i++) {
            var left = leftSorted.get(i);
            var right = rightSorted.get(i);
            sumOfDifferences += Math.abs(left - right);
        }

        return sumOfDifferences;
    }

    @Override
    public Object part2(String input) {
        List<Integer> left = determineLeftInput(input).toList();
        Map<Integer, Long> rightFrequencyMap = determineRightInput(input)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Long similarityScore = 0L;

        for (Integer i : left) {
            similarityScore += i * rightFrequencyMap.getOrDefault(i, 0L);
        }

        return similarityScore;
    }
}
