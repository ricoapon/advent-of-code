package nl.ricoapon.year2022.day4;

import java.util.Arrays;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay4 implements Algorithm {
    private static class Range {
        private final int min;
        private final int max;

        Range(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public static Range of(String range) {
            int min = Integer.parseInt(range.split("-")[0]);
            int max = Integer.parseInt(range.split("-")[1]);
            return new Range(min, max);
        }

        public boolean isContainedIn(Range other) {
            return other.min <= min && max <= other.max;
        }

        public boolean hasAnyOverlapWith(Range other) {
            if (other.min <= min && min <= other.max) {
                return true;
            } else if (other.min <= max && max <= other.max) {
                return true;
            }
            return false;
        }
    }

    @Override
    public Object part1(String input) {
        long result = Arrays.stream(input.split("\r?\n")).filter(this::fullyContainsOther).count();
        return String.valueOf(result);
    }

    private boolean fullyContainsOther(String line) {
        Range first = Range.of(line.split(",")[0]);
        Range second = Range.of(line.split(",")[1]);
        if (first.isContainedIn(second) || second.isContainedIn(first)) {
            return true;
        }

        return false;
    }

    @Override
    public Object part2(String input) {
        long result = Arrays.stream(input.split("\r?\n")).filter(this::overlapAnything).count();
        return String.valueOf(result);
    }

    private boolean overlapAnything(String line) {
        Range first = Range.of(line.split(",")[0]);
        Range second = Range.of(line.split(",")[1]);
        if (first.hasAnyOverlapWith(second) || second.hasAnyOverlapWith(first)) {
            return true;
        }

        return false;
    }
}
