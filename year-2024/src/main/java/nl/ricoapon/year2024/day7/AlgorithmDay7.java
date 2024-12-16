package nl.ricoapon.year2024.day7;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import nl.ricoapon.Pair;
import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay7 implements Algorithm {
    private <T> List<T> copyListWithoutLastElement(List<T> list) {
        List<T> newList = new ArrayList<T>(list);
        newList.remove(list.size() - 1);
        return newList;
    }

    // Recursive method that runs through the possibilities in reverse.
    // We do this so we can check if the total number can be divided by
    // the last integer. If not, we know it can never be a multiple.
    private boolean isPossible(Long sum, List<Long> possibilities) {
        if (possibilities.size() == 0) {
            return sum == 0;
        }

        var last = possibilities.get(possibilities.size() - 1);

        var possible = false;

        if (sum % last == 0) {
            possible = possible || isPossible(sum / last, copyListWithoutLastElement(possibilities));
        }
        if (sum - last >= 0) {
            possible = possible || isPossible(sum - last, copyListWithoutLastElement(possibilities));
        }
        return possible;
    }

    // Recursive method that runs through the possibilities in reverse.
    // We do this so we can check if the total number can be divided by
    // the last integer. If not, we know it can never be a multiple.
    private boolean isPossiblePart2(Long sum, List<Long> possibilities) {
        if (possibilities.size() == 0) {
            return sum == 0;
        }

        var last = possibilities.get(possibilities.size() - 1);

        var possible = false;

        if (sum % last == 0) {
            possible = possible || isPossiblePart2(sum / last, copyListWithoutLastElement(possibilities));
        }
        if (sum - last >= 0) {
            possible = possible || isPossiblePart2(sum - last, copyListWithoutLastElement(possibilities));
        }
        if (String.valueOf(sum).endsWith(String.valueOf(last))) {
            // Math trick to "remove" the integers at the end.
            long lengthOfLast = (long) Math.floor(Math.log10(last)) + 1;
            long newSum = (sum) / ((long) Math.pow(10, lengthOfLast));
            possible = possible || isPossiblePart2(newSum, copyListWithoutLastElement(possibilities));
        }
        return possible;
    }

    private Pair<Long, List<Long>> parseLine(String line) {
        String[] split = line.split(": ");
        return new Pair<>(
            Long.valueOf(split[0]),
            Stream.of(split[1].split(" ")).map(Long::valueOf).toList()
        );
    }

    private List<Pair<Long, List<Long>>> parseInput(String input) {
        return Stream.of(input.split("\\r?\\n")).map(this::parseLine).toList();
    }

    @Override
    public Object part1(String input) {
        return parseInput(input).stream()
            .filter(p -> isPossible(p.getL(), p.getR()))
            .mapToLong(p -> p.getL())
            .sum();
    }

    @Override
    public Object part2(String input) {
        return parseInput(input).stream()
            .filter(p -> isPossiblePart2(p.getL(), p.getR()))
            .mapToLong(p -> p.getL())
            .sum();
    }
}
