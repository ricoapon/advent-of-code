package nl.ricoapon.year2023.day5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public record Almanac(List<Long> initialSeeds, List<ToMap> toMaps) {
    public record Range(long destinationStart, long sourceStart, long range) {
        public static Range of(String line) {
            List<Long> values = Arrays.stream(line.split("\\s+")).map(Long::valueOf).toList();
            return new Range(values.get(0), values.get(1), values.get(2));
        }

        public boolean isSourcePartOfRange(long sourceValue) {
            return sourceStart <= sourceValue && sourceValue <= sourceStart + range - 1;
        }

        public long convertSourceToRange(long sourceValue) {
            return sourceValue - sourceStart + destinationStart;
        }
    }

    public record ToMap(String from, String to, List<Range> ranges) {

        private static final Pattern TO = Pattern.compile("(\\w+)-to-(\\w+) map:");

        public static ToMap of(String lines) {
            List<String> lineList = Arrays.stream(lines.split("\\r?\\n")).collect(Collectors.toCollection(ArrayList::new));
            Matcher toMatcher = TO.matcher(lineList.get(0));
            if (!toMatcher.matches()) {
                throw new RuntimeException("This should not happen");
            }
            lineList.remove(0);

            List<Range> ranges = lineList.stream().map(Range::of).toList();
            return new ToMap(toMatcher.group(1), toMatcher.group(2), ranges);
        }

        public long convertSourceToDestination(long sourceValue) {
            // I assume there is only one range which contains the value, but to be sure we
            // check this explicitely.
            List<Range> matchingRanges = ranges.stream().filter(r -> r.isSourcePartOfRange(sourceValue)).toList();
            if (matchingRanges.size() > 1) {
                throw new RuntimeException("This should not happen");
            } else if (matchingRanges.size() == 1) {
                return matchingRanges.get(0).convertSourceToRange(sourceValue);
            } else {
                return sourceValue;
            }
        }
    }

    public static Almanac of(String input) {
        List<String> blocks = Arrays.stream(input.split("\\r?\\n\\r?\\n")).collect(Collectors.toCollection(ArrayList::new));

        List<Long> seeds = Arrays.stream(blocks.get(0).split(": ")[1].split("\\s+"))
                .map(Long::valueOf)
                .toList();
        blocks.remove(0);

        List<ToMap> toMaps = blocks.stream().map(ToMap::of).toList();

        return new Almanac(seeds, toMaps);
    }

    public long convertFromSeedToLocation(long seed) {
        long value = seed;
        for (ToMap toMap : toMaps) {
            value = toMap.convertSourceToDestination(value);
        }
        return value;
    }
}
