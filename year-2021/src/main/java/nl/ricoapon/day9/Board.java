package nl.ricoapon.day9;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class Board {
    private final List<List<Cell>> board;
    private final int sizeX;
    private final int sizeY;

    public Board(List<List<Cell>> board) {
        this.board = board;
        this.sizeX = board.size();
        this.sizeY = board.get(0).size();
    }

    public static Board of(String input) {
        List<List<Integer>> inputAsIntegerBoard = Arrays.stream(input.split("\n"))
                .map(s -> Arrays.asList(s.split("")))
                .map(l -> l.stream().map(Integer::parseInt).toList())
                .toList();

        int sizeX = inputAsIntegerBoard.size();
        int sizeY = inputAsIntegerBoard.get(0).size();
        List<List<Cell>> board = new ArrayList<>();
        for (int i = 0; i < sizeX; i++) {
            board.add(new ArrayList<>());
        }

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                board.get(x).add(new Cell(inputAsIntegerBoard.get(x).get(y), x, y));
            }
        }

        return new Board(board);
    }

    public Stream<Cell> stream() {
        return board.stream().flatMap(Collection::stream);
    }

    private Cell getCell(int x, int y) {
        return board.get(x).get(y);
    }

    public List<Cell> determineAdjacentCells(Cell cell) {
        List<Cell> adjacentCells = new ArrayList<>();
        int x = cell.x();
        int y = cell.y();

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
}
