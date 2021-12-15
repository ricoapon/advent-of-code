package nl.ricoapon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class GridWithCoordinates<C> {
    private final List<List<Pair<Coordinate2D, C>>> grid;
    private final int sizeX;
    private final int sizeY;

    public GridWithCoordinates(List<List<C>> gridWithoutCoordinates) {
        this.sizeX = gridWithoutCoordinates.size();
        this.sizeY = gridWithoutCoordinates.get(0).size();

        grid = new ArrayList<>();
        for (int x = 0; x < sizeX; x++) {
            List<Pair<Coordinate2D, C>> row = new ArrayList<>();
            grid.add(row);
            for (int y = 0; y < sizeY; y++) {
                row.add(new Pair<>(new Coordinate2D(x, y), gridWithoutCoordinates.get(x).get(y)));
            }
        }
    }

    /**
     * @return The cell at the coordinate {@code (x,y)}.
     */
    public C getCell(int x, int y) {
        return getCell(new Coordinate2D(x, y));
    }

    /**
     * @return The cell at the given coordinate.
     */
    public C getCell(Coordinate2D coordinate) {
        return grid.get(coordinate.x()).get(coordinate.y()).r();
    }

    /**
     * @param c The cell to find.
     * @return The coordinate of the cell.
     */
    public Coordinate2D determineCoordinates(C c) {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (grid.get(x).get(y).r() == c) {
                    return new Coordinate2D(x, y);
                }
            }
        }
        throw new RuntimeException("Grid does not contain given cell");
    }

    /**
     * @return {@link Stream} of all elements in the grid.
     */
    public Stream<Pair<Coordinate2D, C>> stream() {
        return grid.stream().flatMap(Collection::stream);
    }

    /**
     * @param c The given cell.
     * @return {@link Set} of all coordinates that are either horizontally or vertically adjacent to the given cell.
     */
    public Set<Coordinate2D> determineHorizontalAndVerticalAdjacentCoordinates(C c) {
        return determineHorizontalAndVerticalAdjacentCoordinates(determineCoordinates(c));
    }

    /**
     * @param coordinate The given coordinate.
     * @return {@link Set} of all coordinates that are either horizontally or vertically adjacent to the given coordinate.
     */
    public Set<Coordinate2D> determineHorizontalAndVerticalAdjacentCoordinates(Coordinate2D coordinate) {
        Set<Coordinate2D> adjacentCoordinates = new HashSet<>();
        int x = coordinate.x();
        int y = coordinate.y();

        // Top.
        if (y > 0) {
            adjacentCoordinates.add(new Coordinate2D(x, y - 1));
        }
        // Bottom
        if (y < sizeY - 1) {
            adjacentCoordinates.add(new Coordinate2D(x, y + 1));
        }
        // Left.
        if (x > 0) {
            adjacentCoordinates.add(new Coordinate2D(x - 1, y));
        }
        // Right
        if (x < sizeX - 1) {
            adjacentCoordinates.add(new Coordinate2D(x + 1, y));
        }

        return adjacentCoordinates;
    }

    /**
     * @param c The given cell.
     * @return {@link Set} of all coordinates that are diagonally adjacent to the given cell.
     */
    public Set<Coordinate2D> determineDiagonallyAdjacentCoordinates(C c) {
        return determineDiagonallyAdjacentCoordinates(determineCoordinates(c));
    }

    /**
     * @param coordinate The given coordinate.
     * @return {@link Set} of all coordinates that are diagonally adjacent to the given coordinates.
     */
    public Set<Coordinate2D> determineDiagonallyAdjacentCoordinates(Coordinate2D coordinate) {
        Set<Coordinate2D> adjacentCoordinates = new HashSet<>();
        int x = coordinate.x();
        int y = coordinate.y();

        // Top left.
        if (x > 0 && y > 0) {
            adjacentCoordinates.add(new Coordinate2D(x - 1, y - 1));
        }
        // Top right.
        if (x > 0 && y < sizeY - 1) {
            adjacentCoordinates.add(new Coordinate2D(x - 1, y + 1));
        }
        // Bottom left.
        if (x < sizeX - 1 && y > 0) {
            adjacentCoordinates.add(new Coordinate2D(x + 1, y - 1));
        }
        // Bottom right.
        if (x < sizeX - 1 && y < sizeY - 1) {
            adjacentCoordinates.add(new Coordinate2D(x + 1, y + 1));
        }

        return adjacentCoordinates;
    }

    /**
     * @param c The given cell.
     * @return {@link Set} of all coordinates that are adjacent (horizontally, vertically or diagonally) to the given cell.
     */
    public Set<Coordinate2D> determineAllAdjacentCoordinates(C c) {
        return determineAllAdjacentCoordinates(determineCoordinates(c));
    }

    /**
     * @param coordinate The given coordinate.
     * @return {@link Set} of all coordinates that are adjacent (horizontally, vertically or diagonally) to the given coordinate.
     */
    public Set<Coordinate2D> determineAllAdjacentCoordinates(Coordinate2D coordinate) {
        Set<Coordinate2D> adjacentCoordinates = new HashSet<>();
        adjacentCoordinates.addAll(determineHorizontalAndVerticalAdjacentCoordinates(coordinate));
        adjacentCoordinates.addAll(determineDiagonallyAdjacentCoordinates(coordinate));
        return adjacentCoordinates;
    }
}
