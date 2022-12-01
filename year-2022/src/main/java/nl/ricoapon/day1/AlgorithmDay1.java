package nl.ricoapon.day1;

import nl.ricoapon.framework.Algorithm;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class AlgorithmDay1 implements Algorithm {
    @Override
    public String part1(String input) {
        return Arrays.stream(input.split("\r?\n\r?\n"))
                .map(s -> Arrays.stream(s.split("\r?\n")).map(Long::parseLong).reduce(0L, Long::sum))
                .max(Comparator.comparingLong(l -> l)).orElseThrow().toString();
    }

    @Override
    public String part2(String input) {
        List<Long> longs = Arrays.stream(input.split("\r?\n\r?\n"))
                .map(s -> Arrays.stream(s.split("\r?\n")).map(Long::parseLong).reduce(0L, Long::sum))
                .sorted(Comparator.reverseOrder())
                .toList();
        return String.valueOf(longs.get(0) + longs.get(1) + longs.get(2));
    }
}
