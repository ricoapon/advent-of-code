package nl.ricoapon.year2025.day5;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay5 implements Algorithm {
    @Override
    public Object part1(String input) {
        Database database = Database.of(input.split("\\r?\\n\\r?\\n")[0]);
        List<Long> ingredientIds = Stream.of(input.split("\\r?\\n\\r?\\n")[1].split("\\r?\\n")).map(Long::valueOf)
                .toList();

        return ingredientIds.stream().filter(database::contains).count();
    }

    private record Range(long minInclusive, long maxInclusive) {
        public static Range of(String line) {
            long first = Long.valueOf(line.split("-")[0]);
            long second = Long.valueOf(line.split("-")[1]);
            return new Range(first, second);
        }

        public boolean contains(long x) {
            return minInclusive <= x && x <= maxInclusive;
        }

        public boolean overlapsWithRange(Range other) {
            return contains(other.minInclusive) || contains(other.maxInclusive) || other.contains(minInclusive) || other.contains(maxInclusive);
        }

        public long length() {
            return maxInclusive - minInclusive + 1;
        }

        // This method will also work if the other range has no overlap with the current
        // range! In that case, the range itself will be returned.
        public List<Range> cutOutOtherRange(Range other) {
            // We could return 0 ranges, when the other range fully contains this one.
            // We could return 2 ranges, when this range fully contains the other one.
            // We could return 1 range, when just one end overlaps the other.
            // Each of those is handled inside one of the if-statements.
            if (other.minInclusive <= minInclusive && maxInclusive <= other.maxInclusive) {
                return List.of();
            } else if (minInclusive <= other.minInclusive && other.maxInclusive <= maxInclusive) {
                // We return two ranges: the left part and right part. It could be that either
                // has length 0. For example: 3-5 and 3-7 should return 6-7.
                Range left = new Range(minInclusive, other.minInclusive - 1);
                Range right = new Range(other.maxInclusive + 1, maxInclusive);
                return Stream.of(left, right).filter(r -> r.length() > 0).toList();
            }

            long newMinInclusive = minInclusive;
            long newMaxInclusive = maxInclusive;

            if (contains(other.minInclusive)) {
                newMaxInclusive = other.minInclusive - 1;
            }
            if (contains(other.maxInclusive)) {
                newMinInclusive = other.maxInclusive + 1;
            }

            return List.of(new Range(newMinInclusive, newMaxInclusive));
        }
    }

    private record Database(List<Range> ranges) {
        public static Database of(String firstHalfOfInput) {
            List<Range> ranges = Stream.of(firstHalfOfInput.split("\\r?\\n")).map(Range::of).toList();
            return new Database(ranges);
        }

        public boolean contains(long x) {
            return ranges.stream().anyMatch(r -> r.contains(x));
        }
    }

    @Override
    public Object part2(String input) {
        Database database = Database.of(input.split("\\r?\\n\\r?\\n")[0]);
        RangeSum rangeSum = new RangeSum();

        database.ranges().stream().forEach(rangeSum::addRange);

        return rangeSum.length();
    }

    // By construction, we make sure that all the ranges inside this list do NOT
    // overlap.
    private static class RangeSum {
        private List<Range> ranges = new ArrayList<>();

        public void addRange(Range newRange) {
            List<Range> overlappingRanges = ranges.stream().filter(r -> r.overlapsWithRange(newRange)).toList();
            if (overlappingRanges.isEmpty()) {
                ranges.add(newRange);
                return;
            }

            // We will "cut out" parts of the new range that are already covered by the
            // overlapping ranges. Because we know that the list of overlapping ranges never
            // overlaps with itself (by construction), we can do this.
            List<Range> newNotOverlappingRanges = List.of(newRange);
            for (Range overlappingRange : overlappingRanges) {
                newNotOverlappingRanges = newNotOverlappingRanges.stream()
                        .flatMap(r -> r.cutOutOtherRange(overlappingRange).stream()).toList();
            }

            ranges.addAll(newNotOverlappingRanges);
        }

        public long length() {
            return ranges.stream().mapToLong(Range::length).sum();
        }
    }
}
