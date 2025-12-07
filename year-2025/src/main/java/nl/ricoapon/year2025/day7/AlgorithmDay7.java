package nl.ricoapon.year2025.day7;

import java.util.HashMap;
import java.util.Map;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.GridWithCoordinates;
import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay7 implements Algorithm {
    private enum Cell {
        START,
        EMPTY,
        SPLITTER;

        public static Cell of(String s) {
            if (".".equals(s)) {
                return EMPTY;
            } else if ("^".equals(s)) {
                return SPLITTER;
            } else if ("S".equals(s)) {
                return START;
            } else {
                throw new RuntimeException("Found unknown char: '" + s + "'");
            }
        }
    }

    private long determineNumberOfPathsDown(String input, boolean isPart1) {
        GridWithCoordinates<Cell> grid = GridWithCoordinates.ofString(input, Cell::of);
        Coordinate2D start = grid.stream().filter(p -> p.r().equals(Cell.START)).findAny().orElseThrow().l();

        // For part 1, two colliding beams count as 1. For part 2, it is not.
        // ArrayList would be too slow for part 2, so we use a MultiSet (our own implementation).
        Map<Coordinate2D, Long> beams = new HashMap<>();
        beams.merge(start, 1L, Long::sum);
        long nrOfSplittersHit = 0;

        for (int i = 0; i < grid.getSizeY() - 1; i++) {
            Map<Coordinate2D, Long> newBeams = new HashMap<>();

            for (Map.Entry<Coordinate2D, Long> e : beams.entrySet()) {
                var c = e.getKey();
                var count = e.getValue();

                // Check where the beam needs to go based on the cell below.
                var below = new Coordinate2D(c.x() + 1, c.y());
                var cell = grid.getCell(below);
                if (cell == Cell.EMPTY || cell == Cell.START) {
                    newBeams.merge(below, count, Long::sum);
                } else {
                    nrOfSplittersHit++;
                    newBeams.merge(new Coordinate2D(c.x() + 1, c.y() - 1), count, Long::sum);
                    newBeams.merge(new Coordinate2D(c.x() + 1, c.y() + 1), count, Long::sum);
                }
            }

            beams = newBeams;
        }

        if (isPart1) {
            return nrOfSplittersHit;
        }
        return beams.entrySet().stream().mapToLong(e -> e.getValue()).sum();
    }

    @Override
    public Object part1(String input) {
        return determineNumberOfPathsDown(input, true);
    }

    @Override
    public Object part2(String input) {
        return determineNumberOfPathsDown(input, false);
    }
}
