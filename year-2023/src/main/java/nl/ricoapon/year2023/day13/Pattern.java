package nl.ricoapon.year2023.day13;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.GridWithCoordinates;

public record Pattern(GridWithCoordinates<Cell> grid) {
    public enum Cell {
        ASH("."),
        ROCK("#");

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

    public static List<Pattern> of(String input) {
        return Arrays.stream(input.split("\\r?\\n\\r?\\n"))
                .map(s -> new Pattern(GridWithCoordinates.ofString(s, Cell::ofSymbol)))
                .toList();
    }

    public record Row(List<Cell> row) {
        // Returns number of different cells. Only 0 if all cells are equal.
        public int compare(Row o) {
            int totalDifferences = 0;

            for (int i = 0; i < row.size(); i++) {
                if (!row.get(i).equals(o.row.get(i))) {
                    totalDifferences += 1;
                }
            }

            return totalDifferences;
        }
    }

    public List<Row> rows() {
        List<Row> rows = new ArrayList<>();
        for (int i = 0; i < grid.getSizeX(); i++) {
            List<Cell> row = new ArrayList<>();
            for (int j = 0; j < grid.getSizeY(); j++) {
                row.add(grid.getCell(new Coordinate2D(i, j)));
            }
            rows.add(new Row(row));
        }
        return rows;
    }

    public List<Row> columns() {
        List<Row> rows = new ArrayList<>();
        for (int j = 0; j < grid.getSizeY(); j++) {
            List<Cell> row = new ArrayList<>();
            for (int i = 0; i < grid.getSizeX(); i++) {
                row.add(grid.getCell(new Coordinate2D(i, j)));
            }
            rows.add(new Row(row));
        }
        return rows;
    }
}
