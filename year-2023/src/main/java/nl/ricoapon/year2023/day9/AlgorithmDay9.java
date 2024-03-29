package nl.ricoapon.year2023.day9;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay9 implements Algorithm {
    record Sequence(List<Long> numbers) {
        public static List<Sequence> of(String input) {
            return Arrays.stream(input.split("\\r?\\n"))
                    .map(s -> new Sequence(Arrays.stream(s.split("\\s+")).map(Long::valueOf).toList()))
                    .toList();
        }

        public Sequence calculateDifferences() {
            List<Long> differences = new ArrayList<>();
            for (int i = 0; i < numbers.size() - 1; i++) {
                differences.add(numbers.get(i + 1) - numbers.get(i));
            }
            return new Sequence(differences);
        }

        public boolean isAllZeros() {
            return numbers.stream().allMatch(l -> l == 0);
        }

        public long getLastNumber() {
            return numbers.get(numbers.size() - 1);
        }
    }

    @Override
    public Object part1(String input) {
        return Sequence.of(input).stream()
                .mapToLong(this::determineNextValue)
                .sum();
    }

    private long determineNextValue(Sequence sequence) {
        if (sequence.isAllZeros()) {
            return 0;
        }

        return sequence.getLastNumber() + determineNextValue(sequence.calculateDifferences());
    }

    @Override
    public Object part2(String input) {
        return Sequence.of(input).stream()
                .mapToLong(this::determinePreviousValue)
                .sum();
    }

    private long determinePreviousValue(Sequence sequence) {
        if (sequence.isAllZeros()) {
            return 0;
        }

        return sequence.numbers().get(0) - determinePreviousValue(sequence.calculateDifferences());
    }
}
