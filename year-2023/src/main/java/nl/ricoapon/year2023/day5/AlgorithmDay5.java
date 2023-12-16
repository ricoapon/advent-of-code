package nl.ricoapon.year2023.day5;

import nl.ricoapon.framework.Algorithm;

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
        long lowestLocationNumber = Long.MAX_VALUE;
        for (int i = 0; i < almanac.initialSeeds().size(); i += 2) {
            long rangeStart = almanac.initialSeeds().get(i);
            long rangeLength = almanac.initialSeeds().get(i + 1);

            for (long j = 0; j < rangeLength; j++) {
                long actualSeed = rangeStart + j;
                long location = almanac.convertFromSeedToLocation(actualSeed);
                if (location < lowestLocationNumber) {
                    lowestLocationNumber = location;
                }
            }
        }

        return String.valueOf(lowestLocationNumber);
    }
}
