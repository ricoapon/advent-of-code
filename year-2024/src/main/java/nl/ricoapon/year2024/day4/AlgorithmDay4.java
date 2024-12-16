package nl.ricoapon.year2024.day4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.GridWithCoordinates;
import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay4 implements Algorithm {

    private final static List<Coordinate2D> DELTA = List.of(
            new Coordinate2D(1, 0),
            new Coordinate2D(0, 1),
            new Coordinate2D(1, 1),
            new Coordinate2D(-1, 0),
            new Coordinate2D(0, -1),
            new Coordinate2D(-1, -1),
            new Coordinate2D(1, -1),
            new Coordinate2D(-1, 1));

    private final static List<Coordinate2D> DELA_X = List.of(
            new Coordinate2D(1, 1),
            new Coordinate2D(-1, -1),
            new Coordinate2D(1, -1),
            new Coordinate2D(-1, 1));

    private boolean equalsXmas(List<Coordinate2D> coordinates, GridWithCoordinates<String> g) {
        return "XMAS".equals(coordinates.stream().map(c -> g.getCell(c)).collect(Collectors.joining("")));
    }

    private int canMakeXmassFromThisCoordinate(Coordinate2D c, GridWithCoordinates<String> g) {
        if (!"X".equals(g.getCell(c))) {
            return 0;
        }

        return (int) DELTA.stream()
                .map(d -> List.of(c, c.sum(d), c.sum(d.multiply(2)), c.sum(d.multiply(3))))
                .filter(l -> l.stream().allMatch(x -> g.containsCoordindate2D(x)))
                .filter(l -> equalsXmas(l, g))
                .count();
    }

    @Override
    public Object part1(String input) {
        GridWithCoordinates<String> g = GridWithCoordinates.ofString(input, s -> s);

        return g.stream().mapToInt(p -> canMakeXmassFromThisCoordinate(p.l(), g)).sum();
    }

    private boolean canMakeMAS(Coordinate2D c, GridWithCoordinates<String> g) {
        List<String> xmas = DELA_X.stream()
                .map(d -> c.sum(d))
                .filter(g::containsCoordindate2D)
                .map(x -> g.getCell(x))
                // We need a mutable list.
                .collect(Collectors.toCollection(ArrayList::new));
        
        if (xmas.size() != 4) {
            return false;
        }

        // We check if the bottom right corner equals at least one of the other adjacent corners. If this is not the case,
        // then it could be that we have found MAM and SAS combination. This should not be counted.
        if (!xmas.get(1).equals(xmas.get(2)) && !xmas.get(1).equals(xmas.get(3))) {
            return false;
        }

        Collections.sort(xmas);
        return List.of("M", "M", "S", "S").equals(xmas);
    }

    @Override
    public Object part2(String input) {
        GridWithCoordinates<String> g = GridWithCoordinates.ofString(input, s -> s);

        return g.stream()
                .filter(p -> "A".equals(p.getR()))
                .filter(p -> canMakeMAS(p.getL(), g))
                .count();
    }
}
