package nl.ricoapon;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Class representing a rectangular grid of cells of the same type.
 * @param <C> Class that does not overwrite {@code equals()} or {@code hashCode()}. Two different cells in the grid
 *            must be different.
 */
public class Grid<C> {
    private final List<List<C>> grid;
    private final int sizeX;
    private final int sizeY;

    public static <C> Grid<C> ofString(String input, Function<String, C> convert) {
        List<List<C>> grid = Arrays.stream(input.split("\n"))
                .map(s -> Arrays.asList(s.split("")))
                .map(l -> l.stream().map(convert).toList())
                .toList();
        return new Grid<>(grid);
    }

    public Grid(List<List<C>> grid) {
        this.grid = grid;
        this.sizeX = grid.size();
        this.sizeY = grid.get(0).size();
    }

    /**
     * @return The cell at the coordinate {@code (x,y)}.
     */
    public C getCell(int x, int y) {
        return grid.get(x).get(y);
    }

    /**
     * @param c The cell to find.
     * @return The coordinates {@code (x,y)} of the cell.
     */
    public Pair<Integer, Integer> determineCoordinates(C c) {
        for (int x = 0; x < sizeX; x++) {
            if (grid.get(x).contains(c)) {
                return new Pair<>(x, grid.get(x).indexOf(c));
            }
        }
        throw new RuntimeException("Grid does not contain given cell");
    }

    /**
     * @return {@link Stream} of all elements in the grid.
     */
    public Stream<C> stream() {
        return grid.stream().flatMap(Collection::stream);
    }

    /**
     * @param c The given cell.
     * @return {@link Set} of all cells that are either horizontally or vertically adjacent to the given cell.
     */
    public Set<C> determineHorizontalAndVerticalAdjacentCells(C c) {
        Set<C> adjacentCells = new HashSet<>();
        Pair<Integer, Integer> xy = determineCoordinates(c);
        int x = xy.getL();
        int y = xy.getR();

        // Top.
        if (y > 0) {
            adjacentCells.add(getCell(x, y - 1));
        }
        // Bottom
        if (y < sizeY - 1) {
            adjacentCells.add(getCell(x, y + 1));
        }
        // Left.
        if (x > 0) {
            adjacentCells.add(getCell(x - 1, y));
        }
        // Right
        if (x < sizeX - 1) {
            adjacentCells.add(getCell(x + 1, y));
        }

        return adjacentCells;
    }

    /**
     * @param c The given cell.
     * @return {@link Set} of all cells that are diagonally adjacent to the given cell.
     */
    public Set<C> determineDiagonallyAdjacentCells(C c) {
        Set<C> adjacentCells = new HashSet<>();
        Pair<Integer, Integer> xy = determineCoordinates(c);
        int x = xy.getL();
        int y = xy.getR();

        // Top left.
        if ( x > 0 && y > 0) {
            adjacentCells.add(getCell(x - 1, y - 1));
        }
        // Top right.
        if (x > 0 && y < sizeY - 1) {
            adjacentCells.add(getCell(x - 1, y + 1));
        }
        // Bottom left.
        if (x < sizeX - 1 && y > 0) {
            adjacentCells.add(getCell(x + 1, y - 1));
        }
        // Bottom right.
        if (x < sizeX - 1 && y < sizeY - 1) {
            adjacentCells.add(getCell(x + 1, y + 1));
        }

        return adjacentCells;
    }

    /**
     * @param c The given cell.
     * @return {@link Set} of all cells that are adjacent (horizontally, vertically or diagonally) to the given cell.
     */
    public Set<C> determineAllAdjacentCells(C c) {
        Set<C> adjacentCells = new HashSet<>();
        adjacentCells.addAll(determineHorizontalAndVerticalAdjacentCells(c));
        adjacentCells.addAll(determineDiagonallyAdjacentCells(c));
        return adjacentCells;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }
}
