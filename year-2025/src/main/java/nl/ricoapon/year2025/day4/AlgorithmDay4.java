package nl.ricoapon.year2025.day4;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.GridWithCoordinates;
import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay4 implements Algorithm {
    private enum Cell {
        EMPTY,
        PAPER;

        public static Cell of(String s) {
            if (".".equals(s)) {
                return EMPTY;
            }
            return PAPER;
        }
    }

    @Override
    public Object part1(String input) {
        GridWithCoordinates<Cell> g = GridWithCoordinates.ofString(input, Cell::of);
        return findReachablePapers(g).size();
    }

    private Set<Coordinate2D> findReachablePapers(GridWithCoordinates<Cell> g) {
        return g.stream()
                .flatMap(node -> determineAccessiblePapersAround(node.getL(), g).stream())
                .collect(Collectors.toSet());
    }

    private Set<Coordinate2D> determineAccessiblePapersAround(Coordinate2D node, GridWithCoordinates<Cell> g) {
        Set<Coordinate2D> papersAround = g
                .determineHorizontalAndVerticalAdjacentCoordinates(node).stream()
                .filter(c -> g.getCell(c) == Cell.PAPER)
                // We need to take into account that at most 4 papers should be around.
                .filter(c -> countPapersAround(c, g) < 4)
                .collect(Collectors.toSet());

        return papersAround;
    }

    private long countPapersAround(Coordinate2D node, GridWithCoordinates<Cell> g) {
        return g.determineAllAdjacentCoordinates(node).stream()
                .filter(c -> g.getCell(c) == Cell.PAPER)
                .count();
    }

    @Override
    public Object part2(String input) {
        GridWithCoordinates<Cell> g = GridWithCoordinates.ofString(input, Cell::of);
        int nrOfRemovedPapers = 0;
        while (true) {
            Set<Coordinate2D> reachablePapers = findReachablePapers(g);
            if (reachablePapers.isEmpty()) {
                break;
            }

            nrOfRemovedPapers += reachablePapers.size();

            // Create a new grid with papers removed.
            final GridWithCoordinates<Cell> current = g;
            Function<Coordinate2D, Cell> f = (Coordinate2D c) -> {
                if (reachablePapers.contains(c)) {
                    return Cell.EMPTY;
                }
                return current.getCell(c);
            };
            g = g.creatNewGrid(f);
        }

        return nrOfRemovedPapers;
    }
}
