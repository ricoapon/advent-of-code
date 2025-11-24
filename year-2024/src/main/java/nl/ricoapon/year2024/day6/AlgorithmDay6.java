package nl.ricoapon.year2024.day6;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.GridWithCoordinates;
import nl.ricoapon.Pair;
import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay6 implements Algorithm {
    private enum Content {
        START,
        EMPTY,
        FILLED;
    }

    @Override
    public Object part1(String input) {
        GridWithCoordinates<Content> g = GridWithCoordinates.ofString(input, (s) -> {
            if (s.equals(".")) {
                return Content.EMPTY;
            } else if (s.equals("#")) {
                return Content.FILLED;
            } else if (s.equals("^")) {
                return Content.START;
            }
            return Content.EMPTY;
        });

        Coordinate2D start = g.stream().filter(p -> p.getR() == Content.START).findFirst().orElseThrow().getL();
        Coordinate2D direction = new Coordinate2D(-1, 0);

        Set<Coordinate2D> visited = new HashSet<>();
        visited.add(start);

        Coordinate2D current = start;
        while (g.containsCoordindate2D(current)) {
            // Check if we can move to the direction. It can be that many directions are
            // blocked, so we use a while loop. It can be that moving into that direction is
            // going off grid, which is also a "valid" direction.
            while (g.getCellOrNull(current.sum(direction)) == Content.FILLED) {
                if (direction.y() == 0) {
                    direction = new Coordinate2D(0, -1 * direction.x());
                } else {
                    direction = new Coordinate2D(direction.y(), 0);
                }
            }

            current = current.sum(direction);
            visited.add(current);
        }

        // The final visited element is added to the set, but doesn't exist in the grid.
        // So we need to remove this.
        return visited.size() - 1;
    }

    private final boolean runsInALoop(GridWithCoordinates<Content> g, Predicate<Coordinate2D> isFilled) {
        Coordinate2D start = g.stream().filter(p -> p.getR() == Content.START).findFirst().orElseThrow().getL();
        Coordinate2D direction = new Coordinate2D(-1, 0);

        Set<Pair<Coordinate2D, Coordinate2D>> visited = new HashSet<>();
        visited.add(new Pair<>(start, direction));

        Coordinate2D current = start;
        while (g.containsCoordindate2D(current)) {
            // Check if we can move to the direction. It can be that many directions are
            // blocked, so we use a while loop. It can be that moving into that direction is
            // going off grid, which is also a "valid" direction.
            while (isFilled.test(current.sum(direction))) {
                if (direction.y() == 0) {
                    direction = new Coordinate2D(0, -1 * direction.x());
                } else {
                    direction = new Coordinate2D(direction.y(), 0);
                }
            }

            current = current.sum(direction);
            Pair<Coordinate2D, Coordinate2D> p = new Pair<>(current, direction);
            if (visited.contains(p)) {
                return true;
            }

            visited.add(p);
        }

        return false;
    }

    @Override
    public Object part2(String input) {
        GridWithCoordinates<Content> g = GridWithCoordinates.ofString(input, (s) -> {
            if (s.equals(".")) {
                return Content.EMPTY;
            } else if (s.equals("#")) {
                return Content.FILLED;
            } else if (s.equals("^")) {
                return Content.START;
            }
            return Content.EMPTY;
        });

        return g.stream().map(Pair::getL)
                .filter(c -> g.getCell(c) == Content.EMPTY)
                .filter(
                        newlyFilledC -> {
                            return runsInALoop(g, c -> (c.x() == newlyFilledC.x() && c.y() == newlyFilledC.y()) || g.getCellOrNull(c) == Content.FILLED);
                        })
                .count();
    }
}
