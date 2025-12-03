package nl.ricoapon.year2025.day3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay3 implements Algorithm {
    @Override
    public Object part1(String input) {
        return Stream.of(input.split("\\r?\\n")).map(Bank::of).mapToLong(b -> b.determineJoltage(2)).sum();
    }

    private record Bank(List<Integer> values) {
        public static Bank of(String line) {
            return new Bank(Stream.of(line.split("")).map(Integer::parseInt).toList());
        }

        public long determineJoltage(int length) {
            List<Integer> result = new ArrayList<>();
            int i = 0;
            int ignoreLastN = length - 1;
            while (ignoreLastN >= 0) {
                int highest = determineIndexOfHighestIntegerN(i, ignoreLastN);
                result.add(values.get(highest));
                i = highest + 1;
                ignoreLastN--;
            }

            return Long.valueOf(result.stream().map(x -> "" + x).collect(Collectors.joining()));
        }

        // We disregard the last integers, since we need to turn on more digits.
        private int determineIndexOfHighestIntegerN(int startIndex, int ignoreLastN) {
            int max = -1;
            int maxIndex = -1;
            for (int i = startIndex; i < values.size() - ignoreLastN; i++) {
                if (values.get(i) > max) {
                    max = values.get(i);
                    maxIndex = i;
                }
            }

            return maxIndex;
        }
    }

    @Override
    public Object part2(String input) {
        return Stream.of(input.split("\\r?\\n")).map(Bank::of).mapToLong(b -> b.determineJoltage(12)).sum();
    }
}
