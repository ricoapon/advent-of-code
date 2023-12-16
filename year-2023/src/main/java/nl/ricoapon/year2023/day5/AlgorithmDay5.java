package nl.ricoapon.year2023.day5;

import nl.ricoapon.Pair;
import nl.ricoapon.framework.Algorithm;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmDay5 implements Algorithm {
    @Override
    public String part1(String input) {
        Almanac almanac = Almanac.of(input);
        long minLocation = almanac.initialSeeds().stream().mapToLong(almanac::convertFromSeedToLocation).min().orElseThrow();
        return String.valueOf(minLocation);
    }

    @Override
    public String part2(String input) {
        Almanac almanac = Almanac.of(input);
        List<Long> possibleSeeds = determinePossibleSeeds(almanac);
        long minLocation = possibleSeeds.stream().mapToLong(almanac::convertFromSeedToLocation).min().orElseThrow();
        return String.valueOf(minLocation);
    }

    // The path of the lowest answer through all the maps must touch at least one boundary of a range. If we calculate
    // all these boundaries upwards (= starting seed), we know that the answer must be part of this (relatively small) list.
    private List<Long> determinePossibleSeeds(Almanac almanac) {
        List<Long> possibleSeeds = new ArrayList<>();
        List<Pair<Long, Long>> seedRanges = new ArrayList<>();
        for (int i = 0; i < almanac.initialSeeds().size(); i += 2) {
            seedRanges.add(new Pair<>(almanac.initialSeeds().get(i), almanac.initialSeeds().get(i + 1)));
        }

        for (int i = 0; i < almanac.toMaps().size(); i++) {
            Almanac.ToMap toMap = almanac.toMaps().get(i);
            List<Long> mapBoundaries = toMap.ranges().stream().map(Almanac.Range::sourceStart).toList();

            for (Long l : mapBoundaries) {
                // Also for i=0 this works fine! It will just return the seed value.
                long possibleSeed = almanac.convertFromDestinationToFinalSource(l, i - 1);

                // The possible seed can only be a valid answer if it is in any seed range.
                for (Pair<Long, Long> seedRange : seedRanges) {
                    if (seedRange.getL() <= possibleSeed && possibleSeed <= seedRange.getL() + seedRange.getR()) {
                        possibleSeeds.add(possibleSeed);
                    }
                }
            }
        }

        // Any boundary of the seeds can also be a possibility.
        possibleSeeds.addAll(seedRanges.stream().map(Pair::getL).toList());

        return possibleSeeds;
    }
}
