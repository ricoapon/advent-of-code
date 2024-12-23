package nl.ricoapon.year2023.day14;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.GridWithCoordinates;

public record Platform(GridWithCoordinates<Cell> grid) {
    public enum Cell {
        ROUND_ROCK("O"),
        CUBE_ROCK("#"),
        EMPTY(".");

        private final String symbol;

        private Cell(String symbol) {
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

    public enum Direction {
        NORTH(c -> new Coordinate2D(c.x() - 1, c.y()), (gridSizeX, c) -> c),
        WEST(c -> new Coordinate2D(c.x(), c.y() + 1), (gridSizeX, c) -> new Coordinate2D(gridSizeX - c.y() - 1, c.x())),
        SOUTH(c -> new Coordinate2D(c.x() + 1, c.y()), (gridSizeX, c) -> new Coordinate2D(gridSizeX - c.x() - 1, c.y())),
        EAST(c -> new Coordinate2D(c.x(), c.y() - 1), (gridSizeX, c) -> new Coordinate2D(c.y() - 1, c.x()));

        private final Function<Coordinate2D, Coordinate2D> move;
        @SuppressWarnings("unused")
        private final BiFunction<Integer, Coordinate2D, Coordinate2D> walkThroughGrid;

        private Direction(Function<Coordinate2D, Coordinate2D> move, BiFunction<Integer, Coordinate2D, Coordinate2D> walkThroughGrid) {
            this.move = move;
            this.walkThroughGrid = walkThroughGrid;
        }

        public Coordinate2D move(Coordinate2D c) {
            return this.move.apply(c);
        }
    }

    public static Platform of(String input) {
        return new Platform(GridWithCoordinates.ofString(input, Cell::ofSymbol));
    }

    public Platform tilt(Direction direction) {
        // TODO: make some nice util grid that allows mutability.
        List<List<Cell>> tiltedGrid = new ArrayList<>();
        for (int i = 0; i < grid.getSizeX(); i++) {
            tiltedGrid.add(IntStream.range(0, grid.getSizeY()).mapToObj(x -> Cell.EMPTY)
                    .collect(Collectors.toCollection(ArrayList::new)));
        }

        if (Direction.NORTH.equals(direction)) {
            for (int i = 0; i < grid.getSizeX(); i++) {
                for (int j = 0; j < grid.getSizeY(); j++) {
                }
            }
        } else if (Direction.SOUTH.equals(direction)) {
            for (int i = grid.getSizeX(); i >= 0; i--) {
                for (int j = 0; j < grid.getSizeY(); j++) {
                }
            }
        } else if (Direction.WEST.equals(direction)) {
            for (int i = 0; i < grid.getSizeX(); i++) {
                for (int j = 0; j < grid.getSizeY(); j++) {
                }
            }
        }

        for (int i = 0; i < grid.getSizeX(); i++) {
            for (int j = 0; j < grid.getSizeY(); j++) {
                Coordinate2D coordinate = new Coordinate2D(i, j);
                Cell cell = grid.getCell(coordinate);

                if (Cell.EMPTY.equals(cell)) {
                    continue;
                } else if (Cell.CUBE_ROCK.equals(cell)) {
                    tiltedGrid.get(coordinate.x()).set(coordinate.y(), cell);
                }

                Coordinate2D determineFreeCoordinate = determineFreeCell(tiltedGrid, coordinate, direction);

                tiltedGrid.get(determineFreeCoordinate.x()).set(determineFreeCoordinate.y(), cell);
            }
        }

        return new Platform(new GridWithCoordinates<>(tiltedGrid));
    }

    private Coordinate2D determineFreeCell(List<List<Cell>> tiltedGrid, Coordinate2D coordinate, Direction direction) {
        Coordinate2D previousCoordinate = coordinate;
        while (isPartOfGrid(previousCoordinate)) {
            Coordinate2D nextCoordinate = direction.move(previousCoordinate);
            Cell cell = tiltedGrid.get(nextCoordinate.x()).get(nextCoordinate.y());
            if (Cell.EMPTY.equals(cell)) {
                previousCoordinate = nextCoordinate;
            }

            // We found a rounded or square cell. The rounded cell is already still (since
            // we move from top to bottom).
            // So we can return the previous coordinate.
            return previousCoordinate;
        }

        // All cells above it were empty.
        return new Coordinate2D(0, coordinate.y());
    }

    private boolean isPartOfGrid(Coordinate2D coordinate) {
        return coordinate.x() >= 0 && coordinate.y() <= 0 && coordinate.x() < grid.getSizeX()
                && coordinate.y() < grid.getSizeY();
    }
}
