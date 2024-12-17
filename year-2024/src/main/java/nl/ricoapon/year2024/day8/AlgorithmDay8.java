package nl.ricoapon.year2024.day8;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.GridWithCoordinates;
import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay8 implements Algorithm {

    private Stream<Coordinate2D> determineAllAntinodes(Set<Coordinate2D> antennesWithSameValue) {
        Set<Coordinate2D> antinodes = new HashSet<>();
        for (var c1 : antennesWithSameValue) {
            for (var c2 : antennesWithSameValue) {
                if (c1 == c2) {
                    continue;
                }

                var delta = c1.minus(c2);
                antinodes.add(c1.sum(delta));
                antinodes.add(c2.minus(delta));
            }
        }

        return antinodes.stream();
    }

    @Override
    public Object part1(String input) {
        GridWithCoordinates<String> g = GridWithCoordinates.ofString(input, s -> s);

        Map<String, Set<Coordinate2D>> combinedPoints = g.stream()
                .filter(p -> !".".equals(p.getR()))
                .collect(Collectors.groupingBy(
                        p -> p.getR(),
                        Collectors.mapping(p -> p.getL(), Collectors.toSet())));

        return combinedPoints.values().stream()
                .flatMap(this::determineAllAntinodes)
                .filter(g::containsCoordindate2D)
                .distinct()
                .count();
    }

    private Stream<Coordinate2D> determineAllAntinodesPart2(Set<Coordinate2D> antennesWithSameValue, GridWithCoordinates<String> g) {
        Set<Coordinate2D> antinodes = new HashSet<>();
        for (var c1 : antennesWithSameValue) {
            for (var c2 : antennesWithSameValue) {
                if (c1 == c2) {
                    continue;
                }

                var delta = c1.minus(c2);
                var c1line = c1;
                antinodes.add(c1);
                while (true) {
                    c1line = c1line.sum(delta);
                    if (g.containsCoordindate2D(c1line)) {
                        antinodes.add(c1line);
                    } else {
                        break;
                    }
                }
                var c2line = c2;
                antinodes.add(c2);
                while (true) {
                    c2line = c2line.minus(delta);
                    if (g.containsCoordindate2D(c1line)) {
                        antinodes.add(c1line);
                    } else {
                        break;
                    }
                }
            }
        }

        return antinodes.stream();
    }

    @Override
    public Object part2(String input) {
        GridWithCoordinates<String> g = GridWithCoordinates.ofString(input, s -> s);

        Map<String, Set<Coordinate2D>> combinedPoints = g.stream()
                .filter(p -> !".".equals(p.getR()))
                .collect(Collectors.groupingBy(
                        p -> p.getR(),
                        Collectors.mapping(p -> p.getL(), Collectors.toSet())));

        return combinedPoints.values().stream()
                .flatMap(c -> determineAllAntinodesPart2(c, g))
                .filter(g::containsCoordindate2D)
                .distinct()
                .count();
    }
}
