package nl.ricoapon.year2023.day16;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.GridWithCoordinates;
import nl.ricoapon.Pair;
import nl.ricoapon.framework.Algorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;

public class AlgorithmDay16 implements Algorithm {
    public enum Direction {
        UP(c -> new Coordinate2D(c.x() - 1, c.y())),
        DOWN(c -> new Coordinate2D(c.x() + 1, c.y())),
        LEFT(c -> new Coordinate2D(c.x(), c.y() - 1)),
        RIGHT(c -> new Coordinate2D(c.x(), c.y() + 1));

        private final Function<Coordinate2D, Coordinate2D> nextStep;

        Direction(Function<Coordinate2D, Coordinate2D> nextStep) {
            this.nextStep = nextStep;
        }

        public Coordinate2D next(Coordinate2D c) {
            return nextStep.apply(c);
        }
    }

    public enum Cell {
        SPLIT_VERTICAL("|"),
        SPLIT_HORIZONTAL("-"),
        MIRROR_FORWARD("/"),
        MIRROR_BACKWARD("\\"),
        EMPTY(".");

        private final String symbol;

        Cell(String symbol) {
            this.symbol = symbol;
        }

        public static Cell ofSymbol(String s) {
            for (Cell c : Cell.values()) {
                if (c.symbol.equals(s)) {
                    return c;
                }
            }

            throw new RuntimeException();
        }
    }

    public static class CellContent {
        Set<Direction> lightBeams = new HashSet<>();
        final Cell cell;

        public CellContent(String symbol) {
            this.cell = Cell.ofSymbol(symbol);
        }
    }

    @Override
    public Object part1(String input) {
        GridWithCoordinates<CellContent> grid = GridWithCoordinates.ofString(input, CellContent::new);

        // The direction indicates where we are going (so not from which part of the
        // cell we enter).
        // We start at the top corner, and we go from left to right. So we fill in right
        // as the direction.
        return countEnergized(new Coordinate2D(0, 0), Direction.RIGHT, grid);
    }

    private long countEnergized(Coordinate2D startingCoordinate, Direction startingDirection,
                                GridWithCoordinates<CellContent> grid) {

        // Other algorithms can mess up the light beams set. So we need to clear it
        // before processing.
        grid.stream().forEach(p -> p.getR().lightBeams = new HashSet<>());

        Queue<Pair<Coordinate2D, Direction>> queue = new LinkedList<>();
        queue.add(new Pair<>(startingCoordinate, startingDirection));

        while (!queue.isEmpty()) {
            Pair<Coordinate2D, Direction> pair = queue.poll();
            Coordinate2D coordinate = pair.getL();
            Direction direction = pair.getR();

            // Light beams can move off the grid. If so, we just stop parsing. No need to do
            // anything with this.
            if (!gridContainsCell(grid, coordinate)) {
                continue;
            }

            CellContent cellContent = grid.getCell(coordinate);

            // Any light beam we have already calculated on this coordinate with this
            // direction can be ignored.
            // If we would not do this, we might end up with infinite loops.
            if (cellContent.lightBeams.contains(direction)) {
                continue;
            }

            cellContent.lightBeams.add(direction);

            if (Cell.SPLIT_VERTICAL.equals(cellContent.cell)
                    && (Direction.LEFT.equals(direction) || Direction.RIGHT.equals(direction))) {
                // We split the beam and go up and down.
                queue.add(new Pair<>(Direction.UP.next(coordinate), Direction.UP));
                queue.add(new Pair<>(Direction.DOWN.next(coordinate), Direction.DOWN));
            } else if (Cell.SPLIT_HORIZONTAL.equals(cellContent.cell)
                    && (Direction.UP.equals(direction) || Direction.DOWN.equals(direction))) {
                // We split the beam and go left and right.
                queue.add(new Pair<>(Direction.LEFT.next(coordinate), Direction.LEFT));
                queue.add(new Pair<>(Direction.RIGHT.next(coordinate), Direction.RIGHT));
            } else if (Cell.MIRROR_FORWARD.equals(cellContent.cell)) {
                Direction newDirection = switch (direction) {
                    case UP -> Direction.RIGHT;
                    case DOWN -> Direction.LEFT;
                    case LEFT -> Direction.DOWN;
                    case RIGHT -> Direction.UP;
                };

                queue.add(new Pair<>(newDirection.next(coordinate), newDirection));
            } else if (Cell.MIRROR_BACKWARD.equals(cellContent.cell)) {
                Direction newDirection = switch (direction) {
                    case UP -> Direction.LEFT;
                    case DOWN -> Direction.RIGHT;
                    case LEFT -> Direction.UP;
                    case RIGHT -> Direction.DOWN;
                };

                queue.add(new Pair<>(newDirection.next(coordinate), newDirection));
            } else {
                // For any other situation (empty or go straight through a split), we just
                // continue with the beam.
                queue.add(new Pair<>(direction.next(coordinate), direction));
            }
        }

        return grid.stream().filter(p -> !p.getR().lightBeams.isEmpty()).count();
    }

    private boolean gridContainsCell(GridWithCoordinates<CellContent> grid, Coordinate2D coordinate) {
        return 0 <= coordinate.x() && coordinate.x() < grid.getSizeX() &&
                0 <= coordinate.y() && coordinate.y() < grid.getSizeY();
    }

    @Override
    public Object part2(String input) {
        GridWithCoordinates<CellContent> grid = GridWithCoordinates.ofString(input, CellContent::new);

        List<Pair<Coordinate2D, Direction>> startingValues = new ArrayList<>();
        for (int i = 0; i < grid.getSizeY(); i++) {
            // From the top.
            startingValues.add(new Pair<>(new Coordinate2D(0, i), Direction.DOWN));
            // From the bottom.
            startingValues.add(new Pair<>(new Coordinate2D(grid.getSizeX() - 1, i), Direction.UP));
        }
        for (int i = 0; i < grid.getSizeX(); i++) {
            // From the left.
            startingValues.add(new Pair<>(new Coordinate2D(i, 0), Direction.RIGHT));
            // From the right.
            startingValues.add(new Pair<>(new Coordinate2D(i, grid.getSizeY() - 1), Direction.LEFT));
        }

        return startingValues.stream().mapToLong(s -> countEnergized(s.getL(), s.getR(), grid)).max()
                .orElseThrow();
    }
}
